package gov.gsa.sam.service;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;

import gov.gsa.sam.exception.BaseApplicationException;

public class MailSenderServiceTest {

	@InjectMocks
	private MailSenderService mailSenderService;

	@Mock
	private JavaMailSender mailSender;

	@Mock
	Environment environment;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void mailSendTest() throws BaseApplicationException {
		when(environment.getProperty(ArgumentMatchers.anyString())).thenReturn("n.e@gmail.com");
		mailSenderService.sendMailMessage("n.e@gsa.gov", "Test", "Test", null);
	}

}
