package com.simms.healthcare;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.simms.healthcare.entity.Dependents;
import com.simms.healthcare.entity.Enrollee;
import com.simms.healthcare.repository.EnrolleeRepository;
import com.simms.healthcare.service.ActivationStatusCode;
import com.simms.healthcare.service.EnrolleeService;
import com.simms.healthcare.util.Util;

@SpringBootApplication
public class ServiceApplication {
	
	private static final Logger log = LoggerFactory.getLogger(ServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);
	}
	
	//@Bean
	public CommandLineRunner demo(EnrolleeRepository enrolleeRepository,
			EnrolleeService enrolleeService) {
		return (args) -> {
			log.info("enrolleeRepository " + enrolleeRepository.toString());

	    	String name = "Brain Cross - " + new Date();
	    	String dependentName1 = "Tom Jones1 " + new Date();
	    	String dependentName2 = "Kim Jones2 " + new Date();
	    	String dependentName3 = "Kim Jones3 " + new Date(); 
	    	String dependentName4 = "Kim Jones4 " + new Date(); 
	    	
	    	String id1 = Util.getNewId();
	    	String id2 = Util.getNewId();
	    	String id3 = Util.getNewId();
	    	String id4 = Util.getNewId();
	    	
			Dependents d1 = new Dependents();
	    	d1.setDependentId(id1);			
			d1.setName(dependentName1);
	    	d1.setBirthdDate(20200101);
			d1.setSsn("001394823");
	    	
			Dependents d2 = new Dependents();
	    	d2.setDependentId(id2);
			d2.setName(dependentName2);
	    	d2.setBirthdDate(20200101);
			d2.setSsn("001394824");
			
			Dependents d3 = new Dependents();
	    	d3.setDependentId(id3);
			d3.setName(dependentName3);
	    	d3.setBirthdDate(20200101);
			d3.setSsn("001394825");
			
			Dependents d4 = new Dependents();
	    	d4.setDependentId(id4);
			d4.setName(dependentName4);
	    	d4.setBirthdDate(20200101);
			d4.setSsn("001394826");
	    	
			List<Dependents> list = new ArrayList<Dependents>();
			list.add(d1);
			list.add(d2);
			list.add(d3);
			list.add(d4);
	    	
	    	Enrollee enrollee = new Enrollee();
	    	String enrolleeId = Util.getNewId();
	    	enrollee.setEnrolleeId(enrolleeId);
	    	enrollee.setName(name);
	    	enrollee.setActivationStatus(ActivationStatusCode.TRUE);
	    	enrollee.setBirthdDate(19900801);
	    	enrollee.setPhoneNumber("8137485545");
	    	enrollee.setSsn("087451254");
	    	enrollee.setDependentList(list);

	    	enrolleeRepository.save(enrollee);

	    	//delete dependent with
	    	Enrollee updatedEnrollee = enrolleeService.removeDependents(enrolleeId, id3);
	    	log.info("----------------------->" + updatedEnrollee.toString());
			
		};
	}

}