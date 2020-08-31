package com.simms.healthcare.rest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.simms.healthcare.businesslogic.ActivationStatusCode;
import com.simms.healthcare.businesslogic.RestRequestCodes;
import com.simms.healthcare.entity.Dependents;
import com.simms.healthcare.entity.Enrollee;
import com.simms.healthcare.model.RequestMessage;
import com.simms.healthcare.util.Util;

/**
 * Test rest controller.
 * @author thoma
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Profile("test")
public class EnrolleeControllerTest {
	
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	
	@Test
	public void contextLoads() throws Exception {
		
		RequestMessage requestMessage = new RequestMessage();
		requestMessage.setUser("testuser");
		requestMessage.setSystem("system test");
		requestMessage.setRequestCode(RestRequestCodes.ADD_ENROLLEE);
		
    	String name = "Kevin Sameuls";
    	String dependentName1 = "Bill Jones";
    	String dependentName2 = "Corey Jones";   	  
    	
		Dependents d1 = new Dependents();
    	d1.setDependentId(Util.generateNewEnrollmentId());			
		d1.setName(dependentName1);
    	d1.setBirthDate(20200101);
		d1.setSsn("101394823");
    	
		Dependents d2 = new Dependents();
    	d2.setDependentId(Util.generateNewEnrollmentId());
		d2.setName(dependentName2);
    	d2.setBirthDate(20200101);
		d2.setSsn("001394824");
    	
		List<Dependents> list = new ArrayList<Dependents>();
		list.add(d1);
		list.add(d2);
    	
    	Enrollee enrollee = new Enrollee();
    	enrollee.setEnrolleeId(Util.generateNewEnrollmentId());
    	enrollee.setName(name);
    	enrollee.setActivationStatus(ActivationStatusCode.TRUE);
    	enrollee.setBirthDate(19900801);
    	enrollee.setPhoneNumber("8137485545");
    	enrollee.setSsn("087451254");
    	enrollee.setDependentList(list);
		
    	requestMessage.setEnrollee(enrollee);
		
		 HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		
		System.out.println(requestMessage);
		    
		HttpEntity<RequestMessage> request = new HttpEntity<>(requestMessage, headers);
		String response = this.restTemplate.postForObject("http://localhost:" + port + "/process-enrollee", 
				requestMessage, String.class);
		assertNotNull(response);
		System.out.println(response);
	}

}
