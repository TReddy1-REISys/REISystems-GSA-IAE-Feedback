package gov.gsa.sam.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import gov.gsa.sam.base.BaseTest;
import gov.gsa.sam.domain.Question;
import gov.gsa.sam.domain.QuestionContent;
import gov.gsa.sam.exception.BaseApplicationException;
import gov.gsa.sam.service.QuestionService;

public class QuestionRestControllerTest extends BaseTest {

	@InjectMocks
	private QuestionRestController questionRestController;

	@Mock
	private QuestionService questionService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
	}

	@Test
	public void getQuestionTest() throws BaseApplicationException {
		when(questionService.getAllQuestions()).thenReturn(defaultQuestionList());
		ResponseEntity<CollectionModel<Question>> questions = questionRestController.getQuestions(StringUtils.EMPTY, StringUtils.EMPTY);
		assertEquals(HttpStatus.OK, questions.getStatusCode());
		Collection<Question> content = questions.getBody().getContent();
		for (Question ques : content) {
			assertNotNull(ques.getCreatedBy());
			assertNotNull(ques.getCreatedDate());
			assertNotNull(ques.getLastModifiedBy());
			assertNotNull(ques.getLastModifiedDate());
			assertNotNull(ques.getQuestionDesc());
			assertNotNull(ques.getQuestionId());
			assertNotNull(ques.getStatus());
			QuestionContent questionOptions = ques.getQuestionOptions();
			assertNotNull(questionOptions.getOptions());
			assertNotNull(questionOptions.getType());
		}
	}

	@Test
	public void getQuestionForMultipleIdTest() throws BaseApplicationException {
		when(questionService.getAllQuestions(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(defaultQuestionList());
		assertEquals(HttpStatus.OK, questionRestController.getQuestions("1", "inactive").getStatusCode());
	}

	@Test
	public void getQuestionWhenException() throws BaseApplicationException {
		when(questionService.getAllQuestions(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenThrow(new NullPointerException());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, questionRestController.getQuestions("1", "active").getStatusCode());
	}

	@Test
	public void getQuestionByIdTest() throws BaseApplicationException {
		when(questionService.getQuestion(ArgumentMatchers.anyLong())).thenReturn(defaultQuestion());
		assertEquals(HttpStatus.OK, questionRestController.getQuestionById(1l).getStatusCode());
	}

	@Test
	public void getQuestionByIdExceptionTest() throws BaseApplicationException {
		when(questionService.getQuestion(ArgumentMatchers.anyLong())).thenThrow(new NullPointerException());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, questionRestController.getQuestionById(1l).getStatusCode());
	}

	@Test
	public void createQuestionTest() throws BaseApplicationException {
		when(questionService.createQuestion(ArgumentMatchers.any(Question.class))).thenReturn(defaultQuestion());
		assertNotNull(questionRestController.createQuestion(defaultQuestion()));
	}

	@Test
	public void createQuestionExceptionTest() throws BaseApplicationException {
		when(questionService.createQuestion(ArgumentMatchers.any(Question.class)))
				.thenThrow(new NullPointerException());
		assertNotNull(questionRestController.createQuestion(defaultQuestion()));
	}

	@Test
	public void updateQuestionTest() throws BaseApplicationException {
		when(questionService.updateQuestion(ArgumentMatchers.any(Question.class))).thenReturn(defaultQuestion());
		assertNotNull(questionRestController.updateQuestion(defaultQuestion()));
	}

	@Test
	public void updateQuestionExceptionTest() throws BaseApplicationException {
		when(questionService.updateQuestion(ArgumentMatchers.any(Question.class)))
				.thenThrow(new NullPointerException());
		assertNotNull(questionRestController.updateQuestion(defaultQuestion()));
	}

	@Test
	public void deleteQuestionSuccessTest() throws BaseApplicationException {
		when(questionService.deleteQuestion(ArgumentMatchers.anyLong())).thenReturn("Successful");
		assertNotNull(questionRestController.deleteQuestion(1l));
	}

	@Test
	public void deleteQuestionFailTest() throws BaseApplicationException {
		when(questionService.deleteQuestion(ArgumentMatchers.anyLong())).thenReturn("Fail");
		assertNotNull(questionRestController.deleteQuestion(1l));
	}

	@Test
	public void deleteQuestionExceptionTest() throws BaseApplicationException {
		when(questionService.deleteQuestion(ArgumentMatchers.anyLong())).thenThrow(new NullPointerException());
		assertNotNull(questionRestController.deleteQuestion(1l));
	}

}
