package com.simms.healthcare.util;

import java.util.UUID;

/**
 * Healthcare util class.
 */
public class Util {
	
	/**
	 * Return a new unique enrollee_id or dependentId.
	 * @return a new enrollee_id or dependentId.
	 */
	public static String generateNewEnrollmentId() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

}
