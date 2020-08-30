package com.simms.healthcare.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simms.healthcare.businesslogic.ResponseCode;
import com.simms.healthcare.businesslogic.RestRequestCodes;
import com.simms.healthcare.businesslogic.SystemMessages;
import com.simms.healthcare.entity.Enrollee;
import com.simms.healthcare.model.RequestMessage;
import com.simms.healthcare.model.ResponseMessage;
import com.simms.healthcare.service.EnrolleeService;

@Service
public class EnrolleeHandler {

	@Autowired
	private EnrolleeService enrolleeService;
	
	/**
	 * Handle request
	 * @param request
	 * @return responseMessage
	 */
	public ResponseMessage handle(RequestMessage request) {
		
		ResponseMessage response = new ResponseMessage();		
		if (request.getRequestCode() != null) {
			response = processRequest(request, response);
			response.setResponseCode(ResponseCode.SUCCESS);
		} else {
			response.setResponseCode(ResponseCode.FAILED);
			response.setResponseMessage(SystemMessages.INVALID_REQUEST_CODE);
		}
		
		return response;
	}

	/**
	 * Process request.
	 * 	ADD_ENROLLEE, MODIFY_ENROLLEE, REMOVE_ENROLLEE, ADD_DEPENDENT, MODIFY_DEPENDENT, REMOVE_DEPENDENT;
	 * @param request request 
	 * @param response response
	 * @return responseMessage
	 */
	private ResponseMessage processRequest(RequestMessage request, ResponseMessage response) {
		RestRequestCodes requestCode = request.getRequestCode();
		
		switch (requestCode) {
		case ADD_ENROLLEE:
			response = handleAddEnrollee(request, response); break;
		case MODIFY_ENROLLEE:
			response = handleModifyEnrollee(request, response); break;
		case REMOVE_ENROLLEE:
			response = handleRemoveEnrollee(request, response); break;
		case ADD_DEPENDENT:
			response = handleAddDependent(request, response); break;
		case MODIFY_DEPENDENT:
			response = handleModifyDependent(request, response); break;
		case REMOVE_DEPENDENT:
			response = handleRemoveDependent(request, response); break;
		}
		
		return response;
	}
	
	/**
	 * Handle remove dependents
	 * @param request
	 * @param response
	 * @return
	 */
	private ResponseMessage handleRemoveDependent(RequestMessage request, ResponseMessage response) {
		List<String> dependentIds = request.getEnrollee().getDependentList().stream().
				map(d -> d.getDependentId()).collect(Collectors.toList());
		this.enrolleeService.removeDependents(request.getEnrollee().getEnrolleeId(), dependentIds); 
		return response;
	}

	/**
	 * Handle modify dependent
	 * @param request
	 * @param response
	 * @return response
	 */
	private ResponseMessage handleModifyDependent(RequestMessage request, ResponseMessage response) {
		Enrollee updateEnrollee = this.enrolleeService.modifyDependents(request.getEnrollee().getEnrolleeId(), 
				request.getEnrollee().getDependentList());
	    response.setEnrollee(updateEnrollee);
		return response;
	}

	/**
	 * Handle add dependent 
	 * @param request
	 * @param response
	 * @return response
	 */
	private ResponseMessage handleAddDependent(RequestMessage request, ResponseMessage response) {
		Enrollee updateEnrollee = this.enrolleeService.addDependents(request.getEnrollee().getEnrolleeId(), 
				request.getEnrollee().getDependentList());
	    response.setEnrollee(updateEnrollee);
		return response;
	}

	/**
	 * Handle remove enrollee
	 * @param request
	 * @param response
	 * @return response
	 */
	private ResponseMessage handleRemoveEnrollee(RequestMessage request, ResponseMessage response) {
		List<String> enrolleeId = new ArrayList<String>();
		enrolleeId.add(request.getEnrollee().getEnrolleeId());
		long count = this.enrolleeService.removeEnrolleeById(enrolleeId);
		response.setNumberRecordsUpdated(count);
		return response;
	}

	/**
	 * Handle modify enrollee
	 * @param request
	 * @param response
	 * @return response
	 */
	private ResponseMessage handleModifyEnrollee(RequestMessage request, ResponseMessage response) {
		Enrollee updateEnrollee =  this.enrolleeService.updateEnrollee(request.getEnrollee().getEnrolleeId(), 
				request.getEnrollee().getName(), request.getEnrollee().getActivationStatus(), 
				request.getEnrollee().getBirthdDate(), request.getEnrollee().getPhoneNumber(), 
				request.getEnrollee().getSsn());
	    response.setEnrollee(updateEnrollee);
		return response;
	}

	/**
	 * Handle add enrollee
	 * @param request
	 * @param response
	 * @return response
	 */
	private ResponseMessage handleAddEnrollee(RequestMessage request, ResponseMessage response) {
		Enrollee updateEnrollee = this.enrolleeService.saveEnrollee(request.getEnrollee());
	    response.setEnrollee(updateEnrollee);
		return response;
	}
	
}
