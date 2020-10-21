/**
 * 
 */
package gov.gsa.sam;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import gov.gsa.sam.domain.Question;
import gov.gsa.sam.domain.QuestionContent;
import junit.framework.Assert;

/**
 * @author Nithin.Emanuel
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = gov.gsa.sam.Application.class ,webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QuestionIT {

	@Value("${local.server.port}")  
    int port;
	
	RestTemplate template = new RestTemplate();
	ObjectMapper mapper = new ObjectMapper();
	
	public Question question;
	
	@Before
	public void init() {
		Timestamp currTime = new Timestamp(System.currentTimeMillis());
		List<String> selectedValues = new ArrayList<String>();
		selectedValues.add("FH");
		selectedValues.add("RMS");
		
		question = new Question();
		question.setCreatedBy("Admin");
		question.setLastModifiedBy("Admin");
		question.setCreatedDate(currTime);
		question.setLastModifiedDate(currTime);
		question.setQuestionDesc("Lets Test it?");
		QuestionContent qc = new QuestionContent();
		qc.setType("multiselect");
		qc.setOptions(selectedValues);
		question.setQuestionOptions(qc);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testApplicationUp() throws Exception{
		ResponseEntity<String> response = template.getForEntity("http://localhost:" + port + "/feedback/v1/question", String.class);
		Assert.assertEquals(org.springframework.http.HttpStatus.OK, response.getStatusCode());
	}
	
	public ArrayNode getquestionList(String value) throws Exception{
		
		JsonNode responseJson = mapper.readTree(value);
		
		JsonNode embedded = responseJson.get("_embedded");
		ArrayNode questionList = (ArrayNode) embedded.get("questionList");
				
		return questionList;
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testGetAllQuestion() throws Exception{
		ResponseEntity<String> response = template.getForEntity("http://localhost:" + port + "/feedback/v1/question?questionIds=1", String.class);
		ArrayNode nodes = getquestionList(response.getBody());
		
		for(JsonNode node : nodes){
			Assert.assertEquals(Long.parseLong(node.get("questionId").toString()), 1);
			Assert.assertNotSame(Long.parseLong(node.get("questionId").toString()), 2);
		}	
		
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testGetAllQuestionParams() throws Exception{
		ResponseEntity<String> response = template.getForEntity("http://localhost:" + port + "/feedback/v1/question?questionIds=1,ad", String.class);
		ArrayNode nodes = getquestionList(response.getBody());
		
		for(JsonNode node : nodes){
			Assert.assertEquals(Long.parseLong(node.get("questionId").toString()), 1);
			Assert.assertNotSame(Long.parseLong(node.get("questionId").toString()), 2);
		}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetQuestionById() throws Exception{
		ResponseEntity<String> response = template.getForEntity("http://localhost:" + port + "/feedback/v1/question/1", String.class);
		
		JsonNode responseJson = mapper.readTree(response.getBody());
		
		Assert.assertEquals(Long.parseLong(responseJson.get("questionId").toString()), 1);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testCreateQuestion() throws Exception{
		ResponseEntity<String> response = template.postForEntity("http://localhost:" + port + "/feedback/v1/question/", question, String.class);
		Assert.assertEquals(org.springframework.http.HttpStatus.CREATED, response.getStatusCode());
		
			
		JsonNode responseJson = mapper.readTree(response.getBody());
		
		Assert.assertNotNull(responseJson.get("questionId"));
		
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testUpdateQuestion() throws Exception{
		question.setQuestionId((long)3);
		question.setQuestionDesc("Testing the question?");
		
		HttpEntity<Question> entity = new HttpEntity<Question>(question);
		ResponseEntity<String> response = template.exchange("http://localhost:" + port + "/feedback/v1/question/", HttpMethod.PUT,entity,String.class);
		
		ObjectMapper mapper = new ObjectMapper();
		Assert.assertEquals(org.springframework.http.HttpStatus.CREATED, response.getStatusCode());
		
		JsonNode responseJson = mapper.readTree(response.getBody());
		Assert.assertEquals(Long.parseLong(responseJson.get("questionId").toString()), 3);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testDeleteQuestion() throws Exception {
		HttpEntity<Question> entity = new HttpEntity<Question>(question);
		ResponseEntity<String> response = template.exchange("http://localhost:" + port + "/feedback/v1/question/5", HttpMethod.DELETE,entity,String.class);
		Assert.assertEquals(org.springframework.http.HttpStatus.OK, response.getStatusCode());
		
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testNegCreateQuestion()throws Exception {
		question.setQuestionId((long)1000);
		ResponseEntity<String> response = template.postForEntity("http://localhost:" + port + "/feedback/v1/question/", question, String.class);
		Assert.assertEquals(org.springframework.http.HttpStatus.CREATED, response.getStatusCode());
		
		JsonNode responseJson = mapper.readTree(response.getBody());
		
		Assert.assertNotSame(Long.parseLong(responseJson.get("questionId").toString()), 1000);
		
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testNegUpdateQuestion() throws Exception{
		question.setQuestionId((long) 100);
		
		HttpEntity<Question> entity = new HttpEntity<Question>(question);
		ResponseEntity<String> response = template.exchange("http://localhost:" + port + "/feedback/v1/question/", HttpMethod.PUT,entity,String.class);
		
		Assert.assertEquals(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());		
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testNegDeleteQuestion() throws Exception {
		HttpEntity<Question> entity = new HttpEntity<Question>(question);
		ResponseEntity<String> response = template.exchange("http://localhost:" + port + "/feedback/v1/question/1000", HttpMethod.DELETE,entity,String.class);
		Assert.assertEquals(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

}
