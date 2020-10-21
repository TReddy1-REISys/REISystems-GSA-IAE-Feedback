package gov.gsa.sam.utilities;

import java.io.IOException;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustomTimestampDeserializer extends JsonDeserializer<Timestamp>{
	private static final Logger log = LoggerFactory.getLogger(CustomTimestampDeserializer.class);

	@Override
	public Timestamp deserialize(JsonParser jsonparser, DeserializationContext context)	{
		String dateAsString = null;
		try {
			dateAsString = jsonparser.getText();
		} catch (IOException e) {
			log.error("Following Exception Occured::"+e);
		}
        if(dateAsString == null || dateAsString.trim().isEmpty()){
            return null;
        }
       
       dateAsString = dateAsString.replace('T', ' ');
       //dateAsString = dateAsString.substring(0, dateAsString.lastIndexOf("-"));
       String[] x = dateAsString.split(":");       
       if(x[2] != null && x[2].indexOf('-') > -1){
    	   dateAsString = dateAsString.substring(0, dateAsString.lastIndexOf("-"));
       }
       
       return Timestamp.valueOf(dateAsString);
        //return new Timestamp(CustomDateParser.parse(dateAsString).getTime());
	}

}