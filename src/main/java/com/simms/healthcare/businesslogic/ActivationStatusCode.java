package com.simms.healthcare.businesslogic;

/**
 * Enrollee activation status.
 * @author thoma
 *
 */
public enum ActivationStatusCode {
	TRUE("1"), FALSE("0");
	
    private String code;
    
    private ActivationStatusCode(String code) {
        this.code = code;
    }
 
    public String getCode() {
        return code;
    }
}
