package gov.gsa.sam.domain;

import javax.persistence.AttributeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FeedbackContentConverter implements AttributeConverter<FeedbackContent, String>{

	private final static ObjectMapper objectMapper = new ObjectMapper();
	private static final Logger log = LoggerFactory.getLogger(FeedbackContentConverter.class);

	@Override
	public String convertToDatabaseColumn(FeedbackContent feedbackObj) {
     try {
            return objectMapper.writeValueAsString(feedbackObj);
        } catch (Exception ex) {
        	log.error("Error converting Database column", ex);
            return null;
        }
	}

	@Override
	public FeedbackContent convertToEntityAttribute(String feedbackJSONStr) {
        try {
            return objectMapper.readValue(feedbackJSONStr, FeedbackContent.class);
        } catch (Exception ex) {
        	log.error("Error converting Entity Attribute ", ex);
            return null;
        }
	}
	
}
