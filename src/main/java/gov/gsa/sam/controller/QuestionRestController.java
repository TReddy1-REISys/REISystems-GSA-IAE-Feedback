package gov.gsa.sam.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.gsa.sam.domain.Question;
import gov.gsa.sam.exception.BaseApplicationException;
import gov.gsa.sam.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author Nithin.Emanuel
 * 
 *         QuestionRestController
 *
 */
@RestController
@RequestMapping(value = FeedbackController.BASEPATH + "/v1/question", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Question", description = "Question Operations")
public class QuestionRestController {

	private static final Logger log = LoggerFactory.getLogger(QuestionRestController.class);

	@Autowired
	private QuestionService quesService;

	/**
	 * This method is used to fetch all questions from QuestionService
	 * 
	 * @param qIds
	 * @return ResponseEntity<CollectionModel<Question>>
	 * @throws BaseApplicationException
	 */
	@RequestMapping(method = RequestMethod.GET)
	@Operation(description = "Fetch all Questions")
	@Parameters({
			@Parameter(name = "qIds", description = "Filter questions by Ids (comma seperated Ids)", in = ParameterIn.QUERY),
			@Parameter(name = "status", description = "Filter questions by status", in = ParameterIn.QUERY) })
	public ResponseEntity<CollectionModel<Question>> getQuestions(
			@RequestParam(value = "qIds", required = false) String qIds,
			@RequestParam(value = "status", required = false) String status) throws BaseApplicationException {
		List<Question> allQuestions = new ArrayList<Question>();

		try {
			allQuestions = quesService.getAllQuestions(qIds, status);

			for (Question question : allQuestions) {
				question.add(linkTo(methodOn(QuestionRestController.class)
						.getQuestions(String.valueOf(question.getQuestionId()), status)).withSelfRel());
			}

			Link link = linkTo(methodOn(QuestionRestController.class).getQuestions(qIds, status)).withSelfRel();
			CollectionModel<Question> resources = new CollectionModel<Question>(allQuestions, link);
			log.info("GET / returns all the questions");

			return new ResponseEntity<CollectionModel<Question>>(resources, HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("feedbackService QuestionRestController Error in retrieving questions ", e);
			return new ResponseEntity<CollectionModel<Question>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * This method is used to fetch question for questionId
	 *
	 * @param qId
	 * @return ResponseEntity<Question>
	 * @throws BaseApplicationException
	 */
	@RequestMapping(value = "/{qId}", method = RequestMethod.GET)
	@Operation(description = "Fetch question for questionId provided")
	@Parameters({ @Parameter(name = "qId", description = "Provide the questionId", in = ParameterIn.PATH) })
	public ResponseEntity<Question> getQuestionById(@PathVariable Long qId) throws BaseApplicationException {

		try {
			Question ques = quesService.getQuestion(qId);
			if (ques != null) {
				ques.add(linkTo(methodOn(QuestionRestController.class).getQuestionById(qId)).withSelfRel());
			}
			// else{
			// throw new BaseApplicationException("Record for questionID " + qId
			// + " is not available");
			// }

			log.info("GET /" + qId + " return detail of the following id " + qId);
			return new ResponseEntity<Question>(ques, HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("FeedService - Error in retriving question details ", e);
			return new ResponseEntity<Question>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * This method is used to add question to database
	 * 
	 * @param question
	 * @return ResponseEntity<Question>
	 * @throws BaseApplicationException
	 */
	@RequestMapping(method = RequestMethod.POST)
	@Operation(description = "Create Question")
	public ResponseEntity<Question> createQuestion(@RequestBody Question question) throws BaseApplicationException {
		try {
			Question savedQuestion = quesService.createQuestion(question);
			log.info("/POST Question was successfully added " + savedQuestion.getQuestionId());
			return new ResponseEntity<Question>(savedQuestion, HttpStatus.CREATED);
		} catch (Exception e) {
			log.error("Error while Posting the question ", e);
			return new ResponseEntity<Question>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * This method is used to update questionId
	 * 
	 * @param questionId
	 * @param question
	 * @return ResponseEntity<Question>
	 * @throws BaseApplicationException
	 */
	@RequestMapping(method = RequestMethod.PUT)
	@Operation(description = "Update Question")
	public ResponseEntity<Question> updateQuestion(@RequestBody Question question) throws BaseApplicationException {
		try {
			Question savedQuestion = quesService.updateQuestion(question);
			log.info("/PUT Question was successfully updated " + question.getQuestionId());
			return new ResponseEntity<Question>(savedQuestion, HttpStatus.CREATED);
		} catch (Exception e) {
			log.error("Error in Updating the question ", e);
			return new ResponseEntity<Question>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * This method is used to Delete Question
	 * 
	 * @param questionId
	 * @return ResponseEntity
	 */
	@RequestMapping(value = "/{questionId}", method = RequestMethod.DELETE)
	@Operation(description = "Delete Question")
	public ResponseEntity<Object> deleteQuestion(@PathVariable Long questionId) {
		try {
			String status = quesService.deleteQuestion(questionId);
			if (("Successful").equalsIgnoreCase(status)) {
				log.info("/Delete successfully deleted question Id " + questionId);
			} else {
				log.info("/Delete the question wasnt present");
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error in deleting the question " + e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
