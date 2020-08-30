package com.simms.healthcare.model;

import java.io.Serializable;

import com.simms.healthcare.businesslogic.RestRequestCodes;
import com.simms.healthcare.entity.Enrollee;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Enrollee Request message.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class RequestMessage implements Serializable {
	
	/**
	 * Serialized id.
	 */
	private static final long serialVersionUID = -2016884178622494432L;

	@NonNull
	private String user;
	
	@NonNull
	private String system;
	
	@NonNull
	private RestRequestCodes requestCode;

	@NonNull
	private Enrollee enrollee;
	
}
