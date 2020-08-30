package com.simms.healthcare;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Microservice to handle processing of enrollees.
 * @author thoma
 *
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ServiceApplication {
	
	private static final Logger log = LoggerFactory.getLogger(ServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);
	}

}