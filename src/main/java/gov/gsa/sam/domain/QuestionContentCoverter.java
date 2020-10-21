package gov.gsa.sam.domain;

import javax.persistence.AttributeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class QuestionContentCoverter implements AttributeConverter<QuestionContent, String> {
	private final static ObjectMapper objectMapper = new ObjectMapper();
	private static final Logger log = LoggerFactory.getLogger(QuestionContentCoverter.class);

	@Override
	public String convertToDatabaseColumn(QuestionContent questionObj) {
     try {
            return objectMapper.writeValueAsString(questionObj);
        } catch (Exception ex) {
        	log.error("Error converting Database column", ex);
            return null;
        }
	}

	@Override
	public QuestionContent convertToEntityAttribute(String questionJSONStr) {
        try {
            return objectMapper.readValue(questionJSONStr, QuestionContent.class);
        } catch (Exception ex) {
        	log.error("Error converting Database column", ex);
            return null;
        }
	}

}
