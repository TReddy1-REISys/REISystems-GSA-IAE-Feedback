package gov.gsa.sam.utilities;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustomTimestampSerializer extends JsonSerializer<Timestamp>{
	
	private static final Logger log = LoggerFactory.getLogger(CustomTimestampDeserializer.class);
	
	@Override
	public void serialize(Timestamp value, JsonGenerator gen, SerializerProvider arg2){
		SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		if (value == null) {
            try {
				gen.writeNull();
			} catch (IOException e) {
				log.error("Following Exception Occured::"+e);
			}
        } else {
            try {
            	gen.writeString(FORMATTER.format(value.getTime()));
			} catch (IOException e) {
				log.error("Following Exception Occured::"+e);
			}
        }
		
	}

}
