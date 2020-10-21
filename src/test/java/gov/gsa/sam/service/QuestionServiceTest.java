package gov.gsa.sam.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import gov.gsa.sam.base.BaseTest;
import gov.gsa.sam.domain.Question;
import gov.gsa.sam.exception.BaseApplicationException;
import gov.gsa.sam.repository.QuestionRepository;
import gov.gsa.sam.utilities.Utility;

public class QuestionServiceTest extends BaseTest {

	@InjectMocks
	private QuestionService questionService;

	@Mock
	private QuestionRepository quesRepo;
	
	@Mock
	private Utility utility;
	

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getAllQuestionTest() throws BaseApplicationException {
		when(quesRepo.getAllQuestions()).thenReturn(defaultQuestionList());
		assertNotNull(questionService.getAllQuestions());
	}

	@Test
	public void getQuestionTest() throws BaseApplicationException {
		when(quesRepo.getQuestion(ArgumentMatchers.anyLong())).thenReturn(defaultQuestion());
		assertNotNull(questionService.getQuestion(1l));
	}

	@Test
	public void getAllQuestionWithIdsTest() throws BaseApplicationException {
		when(quesRepo.getAllQuestionsForIds(ArgumentMatchers.anySet())).thenReturn(defaultQuestionList());
		assertNotNull(questionService.getAllQuestions("1,2", "active"));
	}

	@Test
	public void createQuestionTest() throws BaseApplicationException {
		when(quesRepo.saveAndFlush(ArgumentMatchers.any(Question.class))).thenReturn(defaultQuestion());
		assertNotNull(questionService.createQuestion(defaultQuestion()));
	}

	@Test
	public void createQuestionExceptionTest() throws BaseApplicationException {
		when(quesRepo.saveAndFlush(ArgumentMatchers.any(Question.class))).thenThrow(new NullPointerException());
		assertNotNull(questionService.createQuestion(defaultQuestion()));
	}

	@Test
	public void updateQuestionTest() throws BaseApplicationException {
		when(quesRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(defaultQuestion()));
		when(quesRepo.saveAndFlush(ArgumentMatchers.any(Question.class))).thenReturn(defaultQuestion());
		assertNotNull(questionService.updateQuestion(defaultQuestion()));
	}

	@Test
	public void deleteQuestionTest() throws BaseApplicationException {
		assertNotNull(questionService.deleteQuestion(1l));
	}

}
