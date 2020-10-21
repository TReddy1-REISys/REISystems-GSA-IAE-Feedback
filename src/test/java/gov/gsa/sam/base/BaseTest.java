package gov.gsa.sam.base;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import gov.gsa.sam.domain.Feedback;
import gov.gsa.sam.domain.FeedbackContent;
import gov.gsa.sam.domain.Question;
import gov.gsa.sam.domain.QuestionContent;
import gov.gsa.sam.domain.ResponseContent;

public abstract class BaseTest {

	public Question defaultQuestion() {
		Timestamp currTime = new Timestamp(System.currentTimeMillis());
		List<String> selectedValues = new ArrayList<String>();
		selectedValues.add("FH");
		selectedValues.add("RMS");

		Question question = new Question();
		question.setCreatedBy("Admin");
		question.setLastModifiedBy("Admin");
		question.setCreatedDate(currTime);
		question.setLastModifiedDate(currTime);
		question.setQuestionDesc("Lets Test it?");
		question.setQuestionId(1l);
		question.setStatus("active");
		QuestionContent qc = new QuestionContent();
		qc.setType("multiselect");
		qc.setOptions(selectedValues);
		question.setQuestionOptions(qc);
		question.hashCode();
		question.equals(new Feedback());

		return question;
	}

	public Feedback defaultFeedback() {
		Feedback feedback = new Feedback();
		feedback.hashCode();
		feedback.equals(new Feedback());
		// Test
		Timestamp currTime = new Timestamp(System.currentTimeMillis());
		List<String> selectedValues = new ArrayList<String>();
		selectedValues.add("FH");
		selectedValues.add("RMS");

		feedback.setCreatedBy("Admin");
		feedback.setCreatedDate(currTime);
		feedback.setLastmodifiedBy("Admin");
		feedback.setLastmodifiedDate(currTime);
		feedback.setQuestionId((long) 2);
		feedback.setUserId("test@gsa.gov");
		feedback.setFeedbackPath("/text");
		feedback.setFeedbackId(1l);

		FeedbackContent feedbackContent = new FeedbackContent();
		feedbackContent.setType("select");
		feedbackContent.setSelected(selectedValues);
		feedback.setFeedbackResponse(feedbackContent);
		return feedback;
	}

	public List<Question> defaultQuestionList() {
		List<Question> questionList = new ArrayList<>();
		questionList.add(defaultQuestion());
		return questionList;
	}

	public List<Feedback> getFeedbackList() {
		List<Feedback> feedbackList = new ArrayList<>();
		feedbackList.add(defaultFeedback());
		return feedbackList;
	}

	public ResponseContent defaultResponseContent() {
		ResponseContent resCon = new ResponseContent();
		resCon.setFeedbackList(getFeedbackList());
		resCon.setFeedbackPath("/text");
		resCon.setUserId("n.e@gsa.gov");
		return resCon;
	}

}
