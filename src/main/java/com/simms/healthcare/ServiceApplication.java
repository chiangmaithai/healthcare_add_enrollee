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
import com.simms.healthcare.util.Util;

@SpringBootApplication
public class ServiceApplication {
	
	private static final Logger log = LoggerFactory.getLogger(ServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);
	}
	
	//@Bean
	public CommandLineRunner demo(EnrolleeRepository enrolleeRepository) {
		return (args) -> {
			log.info("enrolleeRepository " + enrolleeRepository.toString());
			
	    	String dependentName1 = "Tom Smith " + new Date();
	    	String dependentName2 = "Kim Smith " + new Date();
			Dependents d1 = new Dependents();
	    	d1.setDependentId(Util.getNewId());			
			d1.setName(dependentName1);
	    	d1.setBirthdDate(20200101);
			d1.setSsn("001394823");
	    	
			Dependents d2 = new Dependents();
	    	d2.setDependentId(Util.getNewId());
			d2.setName(dependentName2);
	    	d2.setBirthdDate(20200101);
			d2.setSsn("001394823");
	    	
			List<Dependents> list = new ArrayList<Dependents>();
			list.add(d1);
			list.add(d2);
			
	    	Enrollee enrollee = new Enrollee();
	    	enrollee.setEnrolleeId(Util.getNewId());
	    	enrollee.setName("Susan Smith");
	    	enrollee.setActivationStatus(ActivationStatusCode.TRUE);
	    	enrollee.setBirthdDate(19900801);
	    	enrollee.setPhoneNumber("8137485545");
	    	enrollee.setSsn("087451254");
	    	enrollee.setDependentList(list);

	    	enrolleeRepository.save(enrollee);
	    	
	    	log.info("Search for enrollees");
	    	for(Enrollee member : enrolleeRepository.findAll()) {
	    		log.info(member.toString());
	    	}
	    	log.info("");
	    	log.info("done");
		};
	}

}