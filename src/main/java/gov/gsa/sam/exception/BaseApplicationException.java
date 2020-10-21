package gov.gsa.sam.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 * 
 * @author nithinemanuel
 *
 */
public class BaseApplicationException extends Exception {

	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(BaseApplicationException.class);

	String errorMessage;

	HttpStatus status;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public BaseApplicationException(String errorMessage) {
		this.errorMessage = errorMessage;
		logger.error(errorMessage);
	}

	public BaseApplicationException(HttpStatus status, String errorMessage) {
		this.errorMessage = errorMessage;
		this.status = status;
		logger.error(errorMessage);
	}

	public BaseApplicationException(Throwable cause) {
		logPrintStackTrace(cause);
	}

	public BaseApplicationException() {

	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	private void logPrintStackTrace(Throwable cause) {
		StringWriter errors = new StringWriter();

		cause.printStackTrace(new PrintWriter(errors));
		logger.error(cause.toString());
	}

}
