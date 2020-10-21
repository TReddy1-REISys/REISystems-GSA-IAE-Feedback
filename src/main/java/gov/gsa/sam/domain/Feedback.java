package gov.gsa.sam.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import gov.gsa.sam.utilities.CustomTimestampDeserializer;
import gov.gsa.sam.utilities.CustomTimestampSerializer;

/**
 * 
 * @author Nithin.Emanuel
 *
 */

@Entity
@Table(name="feedback")
@Relation(collectionRelation = "feedbackList")
public class Feedback extends RepresentationModel<Feedback> {
	
	@Id
	@Column(name = "feedback_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long feedbackId;
	
	@Column(name="question_id")
	private Long questionId;
	
	@Column(name="user_id")
	private String userId;
	
	@Column(name = "feedback_path")
	private String feedbackPath;
	
	@JsonSerialize(using = CustomTimestampSerializer.class)
	@JsonDeserialize(using = CustomTimestampDeserializer.class)
	@Column(name="created_date")
	private Timestamp createdDate;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@JsonSerialize(using = CustomTimestampSerializer.class)
	@JsonDeserialize(using = CustomTimestampDeserializer.class)
	@Column(name="last_modified_date")
	private Timestamp lastmodifiedDate;
	
	@Column(name="last_modified_by")
	private String lastmodifiedBy;
	
	@JsonProperty("feedback_response")
	@Column(name= "feedback_response")
	@Convert(converter=FeedbackContentConverter.class)
	private FeedbackContent feedbackResponse;	
	
	/**
	 * 
	 * @return
	 */
	public Long getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(Long feedbackId) {
		this.feedbackId = feedbackId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	public String getFeedbackPath() {
		return feedbackPath;
	}

	public void setFeedbackPath(String feedbackPath) {
		this.feedbackPath = feedbackPath;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getLastmodifiedDate() {
		return lastmodifiedDate;
	}

	public void setLastmodifiedDate(Timestamp lastmodifiedDate) {
		this.lastmodifiedDate = lastmodifiedDate;
	}

	public String getLastmodifiedBy() {
		return lastmodifiedBy;
	}

	public void setLastmodifiedBy(String lastmodifiedBy) {
		this.lastmodifiedBy = lastmodifiedBy;
	}

	public FeedbackContent getFeedbackResponse() {
		return feedbackResponse;
	}

	public void setFeedbackResponse(FeedbackContent feedbackResponse) {
		this.feedbackResponse = feedbackResponse;
	}
	
	@Override
	public int hashCode() {
	  return System.identityHashCode(this);
	}
	public boolean equals(Object object) {        
	 return this==object;
	}

}
