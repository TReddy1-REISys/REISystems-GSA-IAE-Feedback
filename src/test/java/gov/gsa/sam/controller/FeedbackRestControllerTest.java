package gov.gsa.sam.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
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
import gov.gsa.sam.domain.Feedback;
import gov.gsa.sam.domain.ResponseContent;
import gov.gsa.sam.exception.BaseApplicationException;
import gov.gsa.sam.service.FeedbackService;

public class FeedbackRestControllerTest extends BaseTest {

	@InjectMocks
	private FeedbackRestController feedbackRestController;

	@Mock
	private FeedbackService feedbackService;

	private MockHttpServletRequest request = new MockHttpServletRequest();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
	}

	@Test
	public void getAllFeedbackWithIdTest() throws BaseApplicationException {
		when(feedbackService.getAllFeedback(ArgumentMatchers.anyString(), ArgumentMatchers.any(Timestamp.class)))
				.thenReturn(getFeedbackList());
		ResponseEntity<CollectionModel<Feedback>> allFeedback = feedbackRestController.getAllFeedback("1", StringUtils.EMPTY);
		Collection<Feedback> content = allFeedback.getBody().getContent();
		assertEquals(HttpStatus.OK, allFeedback.getStatusCode());
		for (Feedback fe : content) {
			assertNotNull(fe.getCreatedBy());
			assertNotNull(fe.getCreatedDate());
			assertNotNull(fe.getFeedbackId());
			assertNotNull(fe.getFeedbackPath());
			assertNotNull(fe.getLastmodifiedBy());
			assertNotNull(fe.getLastmodifiedDate());
			assertNotNull(fe.getFeedbackResponse().getSelected());
			assertNotNull(fe.getFeedbackResponse().getType());
		}
	}

	@Test
	public void getAllFeedbackTest() throws BaseApplicationException {
		when(feedbackService.getAllFeedback(ArgumentMatchers.any())).thenReturn(getFeedbackList());
		assertEquals(HttpStatus.OK,
				feedbackRestController.getAllFeedback(StringUtils.EMPTY, StringUtils.EMPTY).getStatusCode());
	}

	@Test
	public void getAllFeedbackExceptionTest() throws BaseApplicationException {
		when(feedbackService.getAllFeedback(ArgumentMatchers.any())).thenThrow(new NullPointerException());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,
				feedbackRestController.getAllFeedback(StringUtils.EMPTY, StringUtils.EMPTY).getStatusCode());
	}

	@Test
	public void getFeedbackByIdTest() throws BaseApplicationException {
		when(feedbackService.getFeedbackById(ArgumentMatchers.anyLong())).thenReturn(defaultFeedback());
		assertEquals(HttpStatus.OK, feedbackRestController.getFeedbackById(1l).getStatusCode());
	}

	@Test
	public void getFeedbackByIdExceptionTest() throws BaseApplicationException {
		when(feedbackService.getFeedbackById(ArgumentMatchers.anyLong())).thenThrow(new NullPointerException());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, feedbackRestController.getFeedbackById(1l).getStatusCode());
	}

	@Test
	public void createFeedbackTest() throws BaseApplicationException {
		when(feedbackService.createFeedback(ArgumentMatchers.any(ResponseContent.class))).thenReturn(getFeedbackList());
		ResponseContent resCon = defaultResponseContent();
		assertEquals(HttpStatus.CREATED, feedbackRestController.createFeedback(resCon, request).getStatusCode());
		assertNotNull(resCon.getFeedbackList());
		assertNotNull(resCon.getFeedbackPath());
		assertNotNull(resCon.getUserId());
	}

	@Test
	public void createFeedbackExceptionTest() throws BaseApplicationException {
		when(feedbackService.createFeedback(ArgumentMatchers.any(ResponseContent.class)))
				.thenThrow(new NullPointerException());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,
				feedbackRestController.createFeedback(defaultResponseContent(), request).getStatusCode());
	}

	@Test
	public void updateFeedbackTest() throws BaseApplicationException {
		when(feedbackService.updateFeedback(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Feedback.class)))
				.thenReturn(defaultFeedback());
		assertEquals(HttpStatus.OK, feedbackRestController.updateFeedback(1l, defaultFeedback()).getStatusCode());
	}

	@Test
	public void updateFeedbackExceptionTest() throws BaseApplicationException {
		when(feedbackService.updateFeedback(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Feedback.class)))
				.thenThrow(new NullPointerException());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,
				feedbackRestController.updateFeedback(1l, defaultFeedback()).getStatusCode());
	}

	@Test
	public void deleteFeedbackTest() throws BaseApplicationException {
		when(feedbackService.deleteFeedback(ArgumentMatchers.anyLong())).thenReturn("Successful");
		assertEquals(HttpStatus.OK, feedbackRestController.deleteFeedback(1l).getStatusCode());
	}

	@Test
	public void deleteFeedbackFailTest() throws BaseApplicationException {
		when(feedbackService.deleteFeedback(ArgumentMatchers.anyLong())).thenReturn("Fail");
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, feedbackRestController.deleteFeedback(1l).getStatusCode());
	}

	@Test
	public void deleteFeedbackExceptionTest() throws BaseApplicationException {
		when(feedbackService.deleteFeedback(ArgumentMatchers.anyLong())).thenThrow(new NullPointerException());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, feedbackRestController.deleteFeedback(1l).getStatusCode());
	}

}
