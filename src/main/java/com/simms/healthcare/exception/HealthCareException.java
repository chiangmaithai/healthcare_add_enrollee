package com.simms.healthcare.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.simms.healthcare.model.ResponseMessage;
import com.simms.healthcare.util.Util;

@ControllerAdvice
public class HealthCareException extends ResponseEntityExceptionHandler {

	  @ResponseBody
	  @ExceptionHandler(Exception.class)
	  @ResponseStatus(HttpStatus.NOT_FOUND)
	  protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
		  
		  ResponseMessage bodyOfResponse = new ResponseMessage();
		  bodyOfResponse.setCreateDate(LocalDateTime.now());
		  bodyOfResponse.setMessageId(Util.getNewId());
		  bodyOfResponse.setResponseMessage("Only exposing for demo: " + ex.getLocalizedMessage());
		  
        return handleExceptionInternal(ex, bodyOfResponse, 
		          new HttpHeaders(), HttpStatus.CONFLICT, request);
	  }
	  
	
}
