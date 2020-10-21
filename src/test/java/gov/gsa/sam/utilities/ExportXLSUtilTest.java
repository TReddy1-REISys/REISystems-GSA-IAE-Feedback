package gov.gsa.sam.utilities;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;

import gov.gsa.sam.base.BaseTest;
import gov.gsa.sam.exception.BaseApplicationException;
import gov.gsa.sam.service.QuestionService;

public class ExportXLSUtilTest extends BaseTest {

	@InjectMocks
	private ExportXLSUtil exportXlsUtil;

	@Mock
	private Environment env;

	@Mock
	private QuestionService questionService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void exportXLSTest() throws BaseApplicationException {
		when(env.getProperty(ArgumentMatchers.anyString())).thenReturn("teet");
		when(questionService.getAllQuestions()).thenReturn(defaultQuestionList());
		CustomDateParser.parse("10-12-2019");
		CustomDateDeserializer des = new CustomDateDeserializer();
		exportXlsUtil.exportXLS(getFeedbackList());
	}

}
