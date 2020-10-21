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
import gov.gsa.sam.domain.Feedback;
import gov.gsa.sam.exception.BaseApplicationException;
import gov.gsa.sam.repository.FeedbackRepository;

public class FeedbackServiceTest extends BaseTest {

	@InjectMocks
	private FeedbackService feedbackService;

	@Mock
	private FeedbackRepository feedbackRepo;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

	}

	@Test
	public void getAllFeedbackTest() throws BaseApplicationException {
		when(feedbackRepo.getAllFeedback()).thenReturn(getFeedbackList());
		assertNotNull(feedbackService.getAllFeedback(null));
	}

	@Test
	public void getFeedbackByIdTest() throws BaseApplicationException {
		when(feedbackRepo.getFeedbackById(ArgumentMatchers.anyLong())).thenReturn(defaultFeedback());
		assertNotNull(feedbackService.getFeedbackById(1l));
	}

	@Test
	public void getAllFeedbackWithId() throws BaseApplicationException {
		when(feedbackRepo.getAllFeedbackByIds(ArgumentMatchers.anySet())).thenReturn(getFeedbackList());
		assertNotNull(feedbackService.getAllFeedback("1,2", null));
	}

	@Test
	public void createFeedbackTest() throws BaseApplicationException {
		when(feedbackRepo.saveAndFlush(ArgumentMatchers.any(Feedback.class))).thenReturn(defaultFeedback());
		assertNotNull(feedbackService.createFeedback(defaultResponseContent()));
	}

	@Test
	public void createFeedbackExceptionTest() throws BaseApplicationException {
		when(feedbackRepo.saveAndFlush(ArgumentMatchers.any(Feedback.class))).thenThrow(new NullPointerException());
		assertNotNull(feedbackService.createFeedback(defaultResponseContent()));
	}

	@Test
	public void updateFeedbackTest() throws BaseApplicationException {
		when(feedbackRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(defaultFeedback()));
		when(feedbackRepo.saveAndFlush(ArgumentMatchers.any(Feedback.class))).thenReturn(defaultFeedback());
		assertNotNull(feedbackService.updateFeedback(1l, defaultFeedback()));
	}

	@Test
	public void getFeedbackReportTest() {
		when(feedbackRepo.getAllFeedbackReport()).thenReturn(getFeedbackList());
		assertNotNull(feedbackService.getAllFeedbackReport());
	}

	@Test
	public void deleteFeedbackTest() {
		assertNotNull(feedbackService.deleteFeedback(1l));
	}

}
