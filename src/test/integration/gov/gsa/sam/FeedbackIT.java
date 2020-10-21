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

import gov.gsa.sam.domain.Feedback;
import gov.gsa.sam.domain.FeedbackContent;
import gov.gsa.sam.domain.ResponseContent;
import junit.framework.Assert;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = gov.gsa.sam.Application.class ,webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FeedbackIT {
	
    @Value("${local.server.port}")  
    int port;
    
    public Feedback feedback;
    
    public ResponseContent rs;
	
    RestTemplate template = new RestTemplate();
	
	@Before
	public void init() {
		
		feedback = new Feedback();
		//Test
		Timestamp currTime = new Timestamp(System.currentTimeMillis());
		List<String> selectedValues = new ArrayList<String>(); 
		List<Feedback> feedbackList = new ArrayList<Feedback>();
		selectedValues.add("FH");
		selectedValues.add("RMS");
		
		feedback.setCreatedBy("Admin");
		feedback.setCreatedDate(currTime);
		feedback.setLastmodifiedBy("Admin");
		feedback.setLastmodifiedDate(currTime);
		feedback.setQuestionId((long) 2);
		feedback.setUserId("test@gsa.gov");
		FeedbackContent feedbackContent = new FeedbackContent();
		feedbackContent.setType("select");
		feedbackContent.setSelected(selectedValues);
		feedback.setFeedbackResponse(feedbackContent);
		feedbackList.add(feedback);
		
		rs = new ResponseContent();
		rs.setUserId("test@gsa.gov");
		rs.setFeedbackList(feedbackList);		
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testApplicationUp() throws Exception{
		ResponseEntity<String> response = template.getForEntity("http://localhost:" + port + "/feedback/v1/feedback", String.class);
		Assert.assertEquals(org.springframework.http.HttpStatus.OK, response.getStatusCode());
	}
	
	public ArrayNode getfeedbackList(String value) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		JsonNode responseJson = mapper.readTree(value);
		
		JsonNode embedded = responseJson.get("_embedded");
		ArrayNode feebackList = (ArrayNode) embedded.get("feedbackList");
				
		return feebackList;
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testGetAllFeedback() throws Exception{
		ResponseEntity<String> response = template.getForEntity("http://localhost:" + port + "/feedback/v1/feedback?feedbackIds=1", String.class);
		ArrayNode nodes = getfeedbackList(response.getBody());
		
		for(JsonNode node : nodes){
			Assert.assertEquals(Long.parseLong(node.get("feedbackId").toString()), 1);
			Assert.assertNotSame(Long.parseLong(node.get("feedbackId").toString()), 2);
		}	
		
	}
	//TesT
	@SuppressWarnings("deprecation")
	@Test
	public void testGetAllFeedbackParam() throws Exception{
		ResponseEntity<String> response = template.getForEntity("http://localhost:" + port + "/feedback/v1/feedback?feedbackIds=1,ad", String.class);
		ArrayNode nodes = getfeedbackList(response.getBody());
		
		for(JsonNode node : nodes){
			Assert.assertEquals(Long.parseLong(node.get("feedbackId").toString()), 1);
			Assert.assertNotSame(Long.parseLong(node.get("feedbackId").toString()), 2);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testGetFeedbackById() throws Exception{
		ResponseEntity<String> response = template.getForEntity("http://localhost:" + port + "/feedback/v1/feedback/1", String.class);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode responseJson = mapper.readTree(response.getBody());
		
		Assert.assertEquals(Long.parseLong(responseJson.get("feedbackId").toString()), 1);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testCreateQuestion() throws Exception{
		ResponseEntity<String> response = template.postForEntity("http://localhost:" + port + "/feedback/v1/feedback/", rs, String.class);
		Assert.assertEquals(org.springframework.http.HttpStatus.CREATED, response.getStatusCode());
		
		ObjectMapper mapper = new ObjectMapper();
		
		ArrayNode responseJson = (ArrayNode)mapper.readTree(response.getBody());
		
		for(JsonNode node : responseJson){
			Assert.assertNotNull(node.get("feedbackId"));
		}
		
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testUpdateFeedback() throws Exception{
		feedback.setQuestionId((long) 1);
		
		HttpEntity<Feedback> entity = new HttpEntity<Feedback>(feedback);
		ResponseEntity<String> response = template.exchange("http://localhost:" + port + "/feedback/v1/feedback/10", HttpMethod.PUT,entity,String.class);
		
		ObjectMapper mapper = new ObjectMapper();
		Assert.assertEquals(org.springframework.http.HttpStatus.OK, response.getStatusCode());
		
		JsonNode responseJson = mapper.readTree(response.getBody());
		Assert.assertEquals(Long.parseLong(responseJson.get("questionId").toString()), 1);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testDeleteFeedback() throws Exception {
		HttpEntity<Feedback> entity = new HttpEntity<Feedback>(feedback);
		ResponseEntity<String> response = template.exchange("http://localhost:" + port + "/feedback/v1/feedback/10", HttpMethod.DELETE,entity,String.class);
		Assert.assertEquals(org.springframework.http.HttpStatus.OK, response.getStatusCode());
		
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testNegCreateFeedback()throws Exception {
		feedback.setFeedbackId((long)1000);
		ResponseEntity<String> response = template.postForEntity("http://localhost:" + port + "/feedback/v1/feedback/", rs, String.class);
		Assert.assertEquals(org.springframework.http.HttpStatus.CREATED, response.getStatusCode());
		
		ObjectMapper mapper = new ObjectMapper();
		
		ArrayNode responseJson = (ArrayNode)mapper.readTree(response.getBody());
		
		for(JsonNode node : responseJson){
			Assert.assertNotSame(Long.parseLong(node.get("feedbackId").toString()), 1000);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testNegUpdateFeedback() throws Exception{
		feedback.setQuestionId((long) 1);
		
		HttpEntity<Feedback> entity = new HttpEntity<Feedback>(feedback);
		ResponseEntity<String> response = template.exchange("http://localhost:" + port + "/feedback/v1/feedback/100", HttpMethod.PUT,entity,String.class);
		
		Assert.assertEquals(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());		
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testNegDeleteFeedback() throws Exception {
		HttpEntity<Feedback> entity = new HttpEntity<Feedback>(feedback);
		ResponseEntity<String> response = template.exchange("http://localhost:" + port + "/feedback/v1/feedback/1000", HttpMethod.DELETE,entity,String.class);
		Assert.assertEquals(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}
	
}
