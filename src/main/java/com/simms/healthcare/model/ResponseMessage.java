package com.simms.healthcare.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.simms.healthcare.businesslogic.ResponseCode;
import com.simms.healthcare.entity.Enrollee;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class ResponseMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8505370528799910097L;

	private String messageId;
	
	private ResponseCode responseCode;
	
	private String responseMessage;
	
	private Enrollee enrollee;
	
	private LocalDateTime createDate;
	
}
