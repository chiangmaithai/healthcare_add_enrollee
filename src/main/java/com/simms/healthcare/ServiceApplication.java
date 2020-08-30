package com.simms.healthcare;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Microservice to handle processing of enrollees.
 * @author thoma
 *
 */
@EnableEurekaClient //annotation makes your Spring Boot application act as a Eureka client.
@SpringBootApplication
public class ServiceApplication {
	
	private static final Logger log = LoggerFactory.getLogger(ServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);
	}

}