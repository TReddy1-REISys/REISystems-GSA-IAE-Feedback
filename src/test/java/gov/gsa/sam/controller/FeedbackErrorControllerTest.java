package gov.gsa.sam.controller;

import static org.mockito.Mockito.mock;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.web.context.request.WebRequest;

public class FeedbackErrorControllerTest {

	@InjectMocks
	private FeedbackErrorController feedbackErrorController;

	@Mock
	private ErrorAttributes errorAttributes;

	HttpServletRequest mockRequest = mock(HttpServletRequest.class);
	WebRequest req = mock(WebRequest.class);

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void errorTest() {
		feedbackErrorController.error(mockRequest, req);
	}

	@Test
	public void getPath() {
		feedbackErrorController.getErrorPath();
	}

}
