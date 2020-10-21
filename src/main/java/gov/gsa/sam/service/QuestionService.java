package gov.gsa.sam.service;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.gsa.sam.domain.Question;
import gov.gsa.sam.exception.BaseApplicationException;
import gov.gsa.sam.repository.QuestionRepository;
import gov.gsa.sam.utilities.Constants;
import gov.gsa.sam.utilities.Utility;

/**
 * @author Nithin.Emanuel Question Service Class
 * 
 */

@Service
public class QuestionService {

	@Autowired
	private QuestionRepository quesRepo;

	@Autowired
	private Utility utility;

	private static final Logger log = LoggerFactory.getLogger(QuestionService.class);

	/**
	 * This method is used to fetch all questions from database
	 *
	 * @return List<Question>
	 */
	public List<Question> getAllQuestions() throws BaseApplicationException {
		log.info("Service gets all the questions");
		List<Question> allQuestion = quesRepo.getAllQuestions();
		return allQuestion;
	}

	/**
	 * This method is used fetch question details for the provided question Id
	 * 
	 * @param questionId
	 *            : Long
	 * 
	 * @return Question
	 */
	public Question getQuestion(Long questionId) throws BaseApplicationException {
		log.info("Service gets question detail for " + questionId);
		Question question = quesRepo.getQuestion(questionId);
		return question;
	}

	/**
	 * This method is used to fetch question details for all the provided question
	 * Ids
	 * 
	 * @param questionIds
	 * @param status
	 * 
	 * @return List<Question>
	 */

	public List<Question> getAllQuestions(String questionIds, String status) throws BaseApplicationException {
		log.info("Service gets question details for following ids " + questionIds);

		String validStatus = utility.isValidStatus(status);

		Set<Long> questions = null;
		List<Question> quesResult = null;
		if (StringUtils.isNotBlank(questionIds)) {
			questionIds = questionIds.replaceAll(" ", "");

			String[] quess = questionIds.split(",");
			questions = new HashSet<Long>();

			for (String ques : quess) {
				if (StringUtils.isNumeric(ques)) {
					questions.add(Long.parseLong(ques));
				}
			}
		}
		if (questions == null) {
			if (StringUtils.isNotBlank(validStatus)) {
				quesResult = quesRepo.getQuestionsByStatus(validStatus);
			} else {
				quesResult = quesRepo.getAllQuestions();
			}
		} else {
			if (StringUtils.isNotBlank(validStatus)) {
				quesResult = quesRepo.getQuestionsByStatusAndIds(questions, validStatus);
			} else {
				quesResult = quesRepo.getAllQuestionsForIds(questions);
			}
		}

		return quesResult;
	}

	/**
	 * This method used to Persist question to database
	 * 
	 * @param question
	 * @return Question
	 * @throws BaseApplicationException
	 */
	public Question createQuestion(Question question) throws BaseApplicationException {
		Timestamp currTime = new Timestamp(System.currentTimeMillis());
		question.setCreatedBy(Constants.ADMIN);
		question.setLastModifiedBy(Constants.ADMIN);
		question.setCreatedDate(currTime);
		question.setLastModifiedDate(currTime);

		Question savedQuestion = new Question();
		try {
			savedQuestion = quesRepo.saveAndFlush(question);
			log.info("Question successfully added ID " + savedQuestion.getQuestionId());
		} catch (Exception e) {
			log.error("FeedBack- QuestionSErvice - Question Post error ", e);
		}
		return savedQuestion;
	}

	/**
	 * This method is used to update a question
	 * 
	 * @param question
	 * @return Question
	 * @throws BaseApplicationException
	 */
	public Question updateQuestion(Question question) throws BaseApplicationException {
		Timestamp currTime = new Timestamp(System.currentTimeMillis());

		Optional<Question> optQues = quesRepo.findById(question.getQuestionId());

		Question dbQuestion = null;
		if (optQues.isPresent()) {
			dbQuestion = optQues.get();
		}

		Question savedQuestion;

		if (dbQuestion != null) {
			dbQuestion.setLastModifiedBy(Constants.ADMIN);
			dbQuestion.setLastModifiedDate(currTime);
			dbQuestion.setQuestionDesc(question.getQuestionDesc());
			dbQuestion.setQuestionOptions(question.getQuestionOptions());
			savedQuestion = quesRepo.saveAndFlush(dbQuestion);
		} else {
			log.info("The question for Id " + question.getQuestionId() + " is not found in DB!!");
			throw new BaseApplicationException(
					"The question for Id " + question.getQuestionId() + " is not found in DB!!");
		}
		log.info("Question Id " + savedQuestion.getQuestionId() + " was successfully updated");

		return savedQuestion;
	}

	/**
	 * This method is used to delete question from DB
	 * 
	 * @param questionId
	 * @return String
	 * @throws BaseApplicationException
	 */
	public String deleteQuestion(Long questionId) throws BaseApplicationException {
		try {
			quesRepo.deleteById(questionId);
			log.info("Question " + questionId + " Id successfully deleted from DB");
			return "Successful";
		} catch (Exception e) {
			log.info("The question " + questionId + " Id couldn't be deleted " + e.getMessage());
			log.info("The question " + questionId + " Id couldn't be deleted ", e);
			return "Failure";
		}
	}

}
