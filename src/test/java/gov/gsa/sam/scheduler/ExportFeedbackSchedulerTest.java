package gov.gsa.sam.scheduler;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;

import gov.gsa.sam.base.BaseTest;
import gov.gsa.sam.service.FeedbackService;
import gov.gsa.sam.service.MailSenderService;
import gov.gsa.sam.utilities.ExportXLSUtil;

public class ExportFeedbackSchedulerTest extends BaseTest {

	@InjectMocks
	private ExportFeedbackScheduler scheduler;

	@Mock
	private FeedbackService feedbackService;

	@Mock
	private MailSenderService mailSenderService;

	@Mock
	private ExportXLSUtil exportXLSUtil;

	@Mock
	Environment environment;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void exportFeedbackAndSendEmailTest() {
		when(feedbackService.getAllFeedbackReport()).thenReturn(getFeedbackList());
		when(exportXLSUtil.exportXLS(ArgumentMatchers.anyList())).thenReturn("Ddone");
		when(environment.getProperty(ArgumentMatchers.anyString())).thenReturn("test,test");
		scheduler.exportFeedbackAndSendEmail();
	}

}
