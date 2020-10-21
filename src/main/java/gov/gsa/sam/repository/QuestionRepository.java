package gov.gsa.sam.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import gov.gsa.sam.domain.Question;

@Transactional(readOnly = true)
public interface QuestionRepository extends JpaRepository<Question, Long> {

	@Query(value = "Select q.* from feedbackschema.question q", nativeQuery = true)
	public List<Question> getAllQuestions();

	@Query(value = "Select q.* from feedbackschema.question q where q.question_id = :questionId", nativeQuery = true)
	public Question getQuestion(@Param("questionId") Long questionId);

	@Query(value = "Select q.* from feedbackschema.question q where q.question_id in :questionIds", nativeQuery = true)
	public List<Question> getAllQuestionsForIds(@Param("questionIds") Set<Long> questionIds);

	@Query(value = "Select q.* from feedbackschema.question q where q.status = :status", nativeQuery = true)
	public List<Question> getQuestionsByStatus(@Param("status") String status);
	
	@Query(value = "Select q.* from feedbackschema.question q where q.question_id in :questionIds and q.status = :status", nativeQuery = true)
	public List<Question> getQuestionsByStatusAndIds(@Param("questionIds") Set<Long> questionIds, @Param("status") String status);

}
