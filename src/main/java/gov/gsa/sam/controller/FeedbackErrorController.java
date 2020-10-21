package gov.gsa.sam.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

import io.swagger.v3.oas.annotations.Hidden;

//  TODO Handle Exceptions and return Bad Request and other responses
@Hidden
@RestController
public class FeedbackErrorController implements ErrorController {
	private final ErrorAttributes errorAttributes;
	private static final String PATH = "/error";

	@Autowired
	public FeedbackErrorController(ErrorAttributes errorAttributes) {
		this.errorAttributes = errorAttributes;
	}

	@RequestMapping(value = PATH)
	public Map<String, Object> error(HttpServletRequest request, WebRequest webRequest) {
		return getErrorAttributes(request, webRequest, getTraceParameter(request));
	}

	private static boolean getTraceParameter(HttpServletRequest request) {
		return request.getParameter("trace") != null && (!"false".equalsIgnoreCase(request.getParameter("trace")));
	}

	private Map<String, Object> getErrorAttributes(HttpServletRequest aRequest, WebRequest webRequest,
			boolean includeStackTrace) {
		RequestAttributes requestAttributes = new ServletRequestAttributes(aRequest);
		return errorAttributes.getErrorAttributes(webRequest, includeStackTrace);
	}

	@Override
	public String getErrorPath() {
		return PATH;
	}
}