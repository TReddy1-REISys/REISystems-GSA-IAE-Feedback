package gov.gsa.sam.utilities;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class Utility {
	
	public String isValidStatus(String value) {
		String lowerCaseStr = null;
		if (StringUtils.isNotBlank(value)) {
			if (value.equalsIgnoreCase(Constants.ACTIVE) || value.equalsIgnoreCase(Constants.INACTIVE)) {
				lowerCaseStr = value.toLowerCase(); 
			}
		}
		return lowerCaseStr;
	}

}
