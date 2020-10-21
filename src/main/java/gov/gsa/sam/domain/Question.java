package gov.gsa.sam.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
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
@Table(name="question")
@Embeddable
@Relation(collectionRelation = "questionList")
public class Question extends RepresentationModel<Question> {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="question_id")
	private Long questionId;
	
	@Column(name="question_desc")
	private String questionDesc;
	
	@JsonSerialize(using = CustomTimestampSerializer.class)
	@JsonDeserialize(using = CustomTimestampDeserializer.class)
	@Column(name="created_date")
	private Timestamp createdDate;
	
	@Column(name="created_by")
	private String createdBy;
	
	@Column(name="last_modified_by")
	private String lastModifiedBy;
	
	@JsonSerialize(using = CustomTimestampSerializer.class)
	@JsonDeserialize(using = CustomTimestampDeserializer.class)
	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;
	
	@JsonProperty("question_options")
	@Column(name= "question_options")
	@Convert(converter=QuestionContentCoverter.class)
	private QuestionContent questionOptions;
	
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public String getQuestionDesc() {
		return questionDesc;
	}

	public void setQuestionDesc(String questionDesc) {
		this.questionDesc = questionDesc;
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

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public QuestionContent getQuestionOptions() {
		return questionOptions;
	}

	public void setQuestionOptions(QuestionContent questionOptions) {
		this.questionOptions = questionOptions;
	}		
	
	@Override
	public int hashCode() {
	  return System.identityHashCode(this);
	}
	public boolean equals(Object object) {        
	 return this==object;
	}
}
