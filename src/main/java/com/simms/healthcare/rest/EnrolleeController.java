package com.simms.healthcare.rest;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.simms.healthcare.handler.EnrolleeHandler;
import com.simms.healthcare.model.RequestMessage;
import com.simms.healthcare.model.ResponseMessage;
import com.simms.healthcare.util.Util;

@RestController
public class EnrolleeController {		
	
	@Autowired
	private EnrolleeHandler enrolleeHandler;

	@ResponseBody
	@RequestMapping(value = "/process-enrollee", method = RequestMethod.POST)
	public ResponseMessage processEnrollee(@RequestBody RequestMessage requestMessage) {
		ResponseMessage response = new ResponseMessage();
		
		response = enrolleeHandler.handle(requestMessage);
		response.setMessageId(Util.getNewId());
		response.setCreateDate(LocalDateTime.now());
		
		return response;
	}
	
}
