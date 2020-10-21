/**
 * 
 */
package gov.gsa.sam.domain;

import java.util.List;

/**
 * @author Nithin.Emanuel
 *
 */
public class ResponseContent {
	
	private String userId;
	private List<Feedback> feedbackList;
	private String feedbackPath;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<Feedback> getFeedbackList() {
		return feedbackList;
	}
	public void setFeedbackList(List<Feedback> feedbackList) {
		this.feedbackList = feedbackList;
	}
	public String getFeedbackPath() {
		return feedbackPath;
	}
	public void setFeedbackPath(String feedbackPath) {
		this.feedbackPath = feedbackPath;
	}	
	
}
