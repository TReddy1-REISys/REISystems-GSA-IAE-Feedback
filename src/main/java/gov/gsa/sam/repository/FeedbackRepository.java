/**
 * 
 */
package gov.gsa.sam.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import gov.gsa.sam.domain.Feedback;

/**
 * @author Nithin.Emanuel
 *
 */
@Transactional(readOnly = true)
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

	@Query(value = "Select f.* from feedbackschema.feedback f", nativeQuery = true)
	public List<Feedback> getAllFeedback();

	@Query(value = "Select f.* from feedbackschema.feedback f where f.feedback_id = :feedbackId", nativeQuery = true)
	public Feedback getFeedbackById(@Param("feedbackId") Long feedbackId);

	@Query(value = "Select f.* from feedbackschema.feedback f where f.feedback_id in :feedbackIds", nativeQuery = true)
	public List<Feedback> getAllFeedbackByIds(@Param("feedbackIds") Set<Long> feedbackIds);

	@Query(value = "Select f.*, q.question_desc as question FROM feedbackschema.feedback f, feedbackschema.question q where q.question_id=f.question_id and f.created_date > now() - interval '24 hour' order by f.feedback_id", nativeQuery = true)
	public List<Feedback> getAllFeedbackReport();

	@Query(value = "Select f.* from feedbackschema.feedback f where f.feedback_id in :feedbackIds and f.created_date >= :feedbackDate and f.created_date < current_timestamp", nativeQuery = true)
	public List<Feedback> getAllFeedbackByIdsAndDate(@Param("feedbackIds") Set<Long> feedbackIds,
			@Param("feedbackDate") Timestamp feedbackDate);

	@Query(value = "Select f.* from feedbackschema.feedback f where f.created_date >= :feedbackDate and f.created_date < current_timestamp", nativeQuery = true)
	public List<Feedback> getAllFeedbackByDate(@Param("feedbackDate") Timestamp feedbackDate);
}