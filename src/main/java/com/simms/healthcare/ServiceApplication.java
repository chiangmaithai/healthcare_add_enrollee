package com.simms.healthcare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import com.simms.healthcare.businesslogic.ActivationStatusCode;
import com.simms.healthcare.entity.Dependents;
import com.simms.healthcare.entity.Enrollee;
import com.simms.healthcare.repository.EnrolleeRepository;
import com.simms.healthcare.service.EnrolleeService;
import com.simms.healthcare.util.Util;

/**
 * Microservice to handle processing of enrollees.
 * @author thoma
 *
 */
@SpringBootApplication
public class ServiceApplication {
	
	private static final Logger log = LoggerFactory.getLogger(ServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);
	}

}