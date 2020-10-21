package gov.gsa.sam.utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomDateParser {
    private static final List<String> formats = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(CustomDateParser.class);
    static{
        formats.add("yyyy-MM-dd HH:mm:ss");
        formats.add("yyyy-MM-dd");
        formats.add("yyyy-MM-dd HH:mm a");
        formats.add("EEE MMM d HH:mm:ss z yyyy");
        formats.add("yyyy-MM-dd'T'HH:mm:ss.SSS z");
   }

    public static Date parse(String dateValue) {
        for(String candidateFormat: formats) {
            Date date = parseDate(dateValue, candidateFormat);
            if (date != null) {
                return date;
            }
        }
        return null;
    }

    private static Date parseDate(String dateCandidate, String dateFormat) {
        try {
            return new SimpleDateFormat(dateFormat).parse(dateCandidate);
        } catch (Exception e) {
        	log.error("Error parsing date " + dateCandidate + " Date Formate" + dateFormat ,e);
            return null;
        }
    }
}
