package gov.gsa.sam.config;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 
 * @author nithinemanuel
 *
 */
@Configuration
public class EnvironmentConfig implements EnvironmentAware {

	private Environment env = null;

	@Override
	public void setEnvironment(Environment env) {
		this.env = env;
	}

	public String getProperty(String propertyName) {
		if (env != null) {
			return env.getProperty(propertyName);
		} else {
			return null;
		}
	}
}
