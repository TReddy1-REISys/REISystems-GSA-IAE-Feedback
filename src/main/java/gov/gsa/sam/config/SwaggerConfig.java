package gov.gsa.sam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * 
 * @author nithinemanuel
 **/
@Configuration
// @EnableSwagger2WebMvc
public class SwaggerConfig {
	// @Bean
	// public Docket feedbackApi() {
	// return new
	// Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
	// .apis(RequestHandlerSelectors.basePackage("gov.gsa")).paths(PathSelectors.ant("/feedback/**")).build();
	//
	// }
	//
	// public ApiInfo apiInfo() {
	// return new ApiInfoBuilder().title("Feedback API")
	// .description("Swagger Documentation for all the Feedback Service
	// API").version("1.0.0").build();
	// }

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().components(new Components()).info(
				new Info().title("Feedback API").description("Swagger Documentation for all the Feedback Service API"));
	}

}