package gov.gsa.sam.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.gsa.sam.domain.Feedback;
import gov.gsa.sam.domain.ResponseContent;
import gov.gsa.sam.exception.BaseApplicationException;
import gov.gsa.sam.repository.FeedbackRepository;
import gov.gsa.sam.utilities.Constants;

/**
 * The Feedback Service contains the business logic for working with Feedback
 * Details
 *
 * @version 1.0
 * @author Nithin John Emanuel
 */

@Service
public class FeedbackService {

	private static final Logger log = LoggerFactory.getLogger(FeedbackService.class);

	@Autowired
	private FeedbackRepository feedbackRepo;

	/**
	 * This method fetches all the feedback from the database
	 * 
	 * @return List<Feedback>
	 * @throws BaseApplicationException
	 */
	public List<Feedback> getAllFeedback(Timestamp feedbackDate) throws BaseApplicationException {
		log.info("Service gets all the feedback details");
		List<Feedback> allFeedback = new ArrayList<>();
		if (feedbackDate == null) {
			allFeedback = feedbackRepo.getAllFeedback();
		} else {
			allFeedback = feedbackRepo.getAllFeedbackByDate(feedbackDate);
		}

		return allFeedback;
	}

	/**
	 * This method fetches feedback detail for the provided Id
	 * 
	 * @param feedbackId
	 * @return Feedback
	 * @throws BaseApplicationException
	 */
	public Feedback getFeedbackById(Long feedbackId) throws BaseApplicationException {
		log.info("Service gets detail of feedback for the Id " + feedbackId);
		Feedback feedback = feedbackRepo.getFeedbackById(feedbackId);
		return feedback;
	}

	/**
	 * This method fetches all the feedback details for the provided ids
	 * 
	 * @param feedbackIds
	 * @return List<Feedback>
	 * @throws BaseApplicationException
	 */
	public List<Feedback> getAllFeedback(String feedbackIds, Timestamp feedbackDate) throws BaseApplicationException {
		log.info("Service gets details of feedback for the following ids " + feedbackIds);
		feedbackIds = feedbackIds.replaceAll("\\s+", "");

		String[] ids = feedbackIds.split(",");
		Set<Long> feedbackIdSet = new HashSet<Long>();

		for (String id : ids) {
			if (StringUtils.isNumeric(id)) {
				feedbackIdSet.add(Long.parseLong(id));
			}
		}

		List<Feedback> feedbackList = new ArrayList<>();
		if (feedbackDate == null) {
			feedbackList = feedbackRepo.getAllFeedbackByIds(feedbackIdSet);
		} else {
			feedbackList = feedbackRepo.getAllFeedbackByIdsAndDate(feedbackIdSet, feedbackDate);
		}

		return feedbackList;
	}

	/**
	 * This method is used to persist all the Feedback to the database
	 * 
	 * @param responseContent
	 * @return
	 * @throws BaseApplicationException
	 */
	public List<Feedback> createFeedback(ResponseContent responseContent) throws BaseApplicationException {
		Timestamp currTime = new Timestamp(System.currentTimeMillis());

		List<Feedback> savedList = new ArrayList<Feedback>();

		try {
			for (Feedback feedback : responseContent.getFeedbackList()) {
				feedback.setFeedbackPath(responseContent.getFeedbackPath());
				feedback.setUserId(responseContent.getUserId());
				feedback.setCreatedBy(Constants.ADMIN);
				feedback.setCreatedDate(currTime);
				feedback.setLastmodifiedBy(Constants.ADMIN);
				feedback.setLastmodifiedDate(currTime);

				// Escaping HTML and Javascript for XSS*******
				List<String> selectedList = feedback.getFeedbackResponse().getSelected();
				List<String> newSelectedList = new ArrayList<String>();
				for (String selected : selectedList) {
					newSelectedList.add(StringEscapeUtils.escapeJavaScript(StringEscapeUtils.escapeHtml(selected)));
				}
				feedback.getFeedbackResponse().setSelected(newSelectedList);
				// Escaping HTML and Javascript for XSS*******

				Feedback savedFeedback = feedbackRepo.saveAndFlush(feedback);
				savedList.add(savedFeedback);
			}
			log.info("Successful to persist all the feedback to DB ");
			return savedList;
		} catch (Exception e) {
			log.error("Error while persisting feedbacks to database ", e);
			return savedList;
		}
	}

	/**
	 * This method is used to update a Feedback
	 * 
	 * @param feedbackId
	 * @param feedback
	 * @return Feedback
	 * @throws BaseApplicationException
	 */
	public Feedback updateFeedback(Long feedbackId, Feedback feedback) throws BaseApplicationException {
		Timestamp currTime = new Timestamp(System.currentTimeMillis());
		Optional<Feedback> feedbackOpt = feedbackRepo.findById(feedbackId);
		Feedback dbFeedback = null;
		if (feedbackOpt.isPresent()) {
			dbFeedback = feedbackOpt.get();
		}

		Feedback savedFeedback;

		if (dbFeedback != null) {
			dbFeedback.setLastmodifiedBy(Constants.ADMIN);
			dbFeedback.setLastmodifiedDate(currTime);
			dbFeedback.setFeedbackResponse(feedback.getFeedbackResponse());
			dbFeedback.setQuestionId(feedback.getQuestionId());
			dbFeedback.setUserId(feedback.getUserId());
			dbFeedback.setFeedbackPath(feedback.getFeedbackPath());

			savedFeedback = feedbackRepo.saveAndFlush(dbFeedback);
			log.info("Feedback Id -" + feedbackId + " was successfully updated");
		} else {
			log.info("Feedback Id : " + feedbackId + " is not present in the DB!!");
			throw new BaseApplicationException("Feedback Id :: " + feedbackId + " is not present in the DB!!");
		}

		return savedFeedback;
	}

	public List<Feedback> getAllFeedbackReport() {
		log.info("Service gets all the feedback details");
		List<Feedback> allFeedback = feedbackRepo.getAllFeedbackReport();
		return allFeedback;
	}

	/**
	 * This method is used to delete feedback from DB
	 * 
	 * @param feedbackId
	 * @return String
	 */
	public String deleteFeedback(Long feedbackId) {
		try {
			feedbackRepo.deleteById(feedbackId);
			log.info("Successful to delete feedback Id " + feedbackId);
			return "Successful";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.info("Error in deleting the feedback Id " + feedbackId + " " + e.getMessage());
			log.error("Error in deleting the feedback", e);
			return "Failure";
		}
	}
}
