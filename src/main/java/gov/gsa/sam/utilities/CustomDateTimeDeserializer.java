package gov.gsa.sam.utilities;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustomDateTimeDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser jsonparser, DeserializationContext context)
            throws IOException {
        String dateAsString = jsonparser.getText();
        if(dateAsString == null || dateAsString.trim().isEmpty()){
            return null;
        }

        return CustomDateParser.parse(dateAsString);
    }
}
