package gov.gsa.sam.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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

import gov.gsa.sam.domain.Feedback;
import gov.gsa.sam.domain.ResponseContent;
import gov.gsa.sam.exception.BaseApplicationException;
import gov.gsa.sam.scheduler.ExportFeedbackScheduler;
import gov.gsa.sam.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = FeedbackController.BASEPATH + "/v1/feedback", produces = MediaType.APPLICATION_JSON_VALUE)
// @Ta(tags = "Feedback", description = "Feedback Operations")
@Tag(name = "Feedback", description = "Feedback Operations")
public class FeedbackRestController {

	private static final Logger log = LoggerFactory.getLogger(FeedbackRestController.class);

	@Resource
	Environment environment;

	@Autowired
	private FeedbackService feedbackService;

	@Autowired
	private ExportFeedbackScheduler exportFeedback;

	/**
	 * This method fetches all the feedback details from the Service
	 * 
	 * @param fIds
	 * @return ResponseEntity<CollectionModel<Feedback>>
	 * @throws BaseApplicationException
	 */
	@RequestMapping(method = RequestMethod.GET)
	@Parameters({
			@Parameter(name = "fIds", description = "Provide the feedback ids, comma separated", required = false),
			@Parameter(name = "fdate", description = "Please provide date feedback was submitted in MM-dd-yyyy", required = false) })
	@Operation(description = "Fetch all feedback")
	public ResponseEntity<CollectionModel<Feedback>> getAllFeedback(
			@RequestParam(value = "fIds", required = false) String fIds,
			@RequestParam(value = "fdate", required = false) String fdate) throws BaseApplicationException {
		List<Feedback> feedbackList = new ArrayList<Feedback>();

		try {
			Timestamp timestamp = null;
			if (StringUtils.isNotBlank(fdate)) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
				Date parsedDate = dateFormat.parse(fdate);
				timestamp = new Timestamp(parsedDate.getTime());
			}

			if (StringUtils.isNotBlank(fIds)) {
				feedbackList = feedbackService.getAllFeedback(fIds, timestamp);
			} else {
				feedbackList = feedbackService.getAllFeedback(timestamp);
			}

			for (Feedback feedback : feedbackList) {
				feedback.add(linkTo(methodOn(FeedbackRestController.class)
						.getAllFeedback(String.valueOf(feedback.getFeedbackId()), fdate)).withSelfRel());
			}
			Link link = linkTo(methodOn(FeedbackRestController.class).getAllFeedback(fIds, fdate)).withSelfRel();
			CollectionModel<Feedback> resource = new CollectionModel<Feedback>(feedbackList, link);

			log.info("Get /" + fIds + "returns all the feedback details");
			return new ResponseEntity<CollectionModel<Feedback>>(resource, HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("Error in fetching feedback details ", e);
			return new ResponseEntity<CollectionModel<Feedback>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * This method returns the feedback details for the provided Id
	 * 
	 * @param fId
	 * @return ResponseEntity<Feedback>
	 * @throws BaseApplicationException
	 */
	@RequestMapping(value = "/{fId}", method = RequestMethod.GET)
	@Operation(description = "Fetch question for questionId provided")
	@Parameters({ @Parameter(name = "fId", description = "Provide feedback Id", in = ParameterIn.PATH) })
	public ResponseEntity<Feedback> getFeedbackById(@PathVariable Long fId) throws BaseApplicationException {

		try {
			Feedback feedback = feedbackService.getFeedbackById(fId);

			if (feedback != null) {
				feedback.add(linkTo(methodOn(FeedbackRestController.class).getFeedbackById(fId)).withSelfRel());
			}
			// else {
			// throw new BaseApplicationException("Record for feedbackID " + fId
			// + " is not available");
			// }

			log.info("GET /" + fId + " return detail of the following id " + fId);
			return new ResponseEntity<Feedback>(feedback, HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.info("Error in getting feedback for " + fId);
			log.error("FeedBackService - Error in getting feedback for : ", e);
			return new ResponseEntity<Feedback>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * This method is used to persist feedback to database
	 * 
	 * @param responseContent
	 * @return
	 * @throws BaseApplicationException
	 */
	@RequestMapping(method = RequestMethod.POST)
	@Operation(description = "Create all Feedbacks")
	public ResponseEntity<List<Feedback>> createFeedback(@RequestBody ResponseContent responseContent,
			HttpServletRequest req) throws BaseApplicationException {
		List<Feedback> feedbackList = new ArrayList<Feedback>();
		try {
			String ipAddress = req.getHeader("X-Forwarded-For");
			log.info("***ipAddress***" + ipAddress + "--" + req.getHeader("X-Forwarded-For"));
			if (responseContent != null && responseContent.getFeedbackList() != null
					&& responseContent.getFeedbackList().size() > 0) {
				log.info("***ResponseContent 1***"
						+ responseContent.getFeedbackList().get(0).getFeedbackResponse().getSelected().get(0));
			}
			if (responseContent != null && responseContent.getFeedbackList() != null
					&& responseContent.getFeedbackList().size() > 1) {
				log.info("***ResponseContent 2***"
						+ responseContent.getFeedbackList().get(1).getFeedbackResponse().getSelected().get(0));
			}
			feedbackList = feedbackService.createFeedback(responseContent);
			log.info("/Post Successful to post all the responses ");
			return new ResponseEntity<List<Feedback>>(feedbackList, HttpStatus.CREATED);
		} catch (Exception e) {
			log.error("/Post Failure in posting all the responses ", e);
			return new ResponseEntity<List<Feedback>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * This method is used to update feedback
	 * 
	 * @param feedbackId
	 * @param feedback
	 * @return ResponseEntity<Feedback>
	 * @throws BaseApplicationException
	 */
	@RequestMapping(value = "/{feedbackId}", method = RequestMethod.PUT)
	@Operation(description = "Update Feedback")
	public ResponseEntity<Feedback> updateFeedback(@PathVariable Long feedbackId, @RequestBody Feedback feedback)
			throws BaseApplicationException {
		try {
			Feedback savedFeedback = feedbackService.updateFeedback(feedbackId, feedback);
			log.info("/Put successfully updated feedback Id " + feedbackId);
			return new ResponseEntity<Feedback>(savedFeedback, HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.info("/Put Error in performing the update on feedback " + feedbackId + "  " + e.getMessage());
			log.error("FeedBackServuce update Feedback Exception occured ", e);
			return new ResponseEntity<Feedback>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * This method is used to Delete feedback
	 * 
	 * @param feedbackId
	 * @return ResponseEntity<Object>
	 * @throws BaseApplicationException
	 */
	@RequestMapping(value = "/{feedbackId}", method = RequestMethod.DELETE)
	@Operation(description = "Delete Feedback")
	public ResponseEntity<Object> deleteFeedback(@PathVariable Long feedbackId) throws BaseApplicationException {

		try {
			String status = feedbackService.deleteFeedback(feedbackId);
			if (("Successful").equalsIgnoreCase(status)) {
				log.info("Successful in deleting Feedback Id " + feedbackId);
				return new ResponseEntity<Object>(HttpStatus.OK);
			} else {
				log.info("Feedback Id " + feedbackId + " wasnt found in DB");
				return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {
			log.info("Failure to delete the feedback Id " + feedbackId + " " + e.getMessage());
			log.error("Failure to delete - logs ", e);
			return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public void exportXLS() {
		exportFeedback.exportFeedbackAndSendEmail();
	}

}
