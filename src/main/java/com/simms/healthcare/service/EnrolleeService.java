package com.simms.healthcare.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.simms.healthcare.entity.Dependents;
import com.simms.healthcare.entity.Enrollee;
import com.simms.healthcare.repository.EnrolleeRepository;

@Service
public class EnrolleeService {
	
	@Autowired
	private EnrolleeRepository enrolleeRepository; 

	public void addDependents() {
		
	}
	
	public void modifyDependents() {
		
	}
	
	/**
	 * Remove one or more dependents and return new Enrollee.
	 * @param enrolleeId enrolleeId.
	 * @param dependentId one or more dependent ids to remove.
	 * @return new Enrollee or null/empty if enrollee cannot be found.
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Enrollee removeDependents(String enrolleeId, String... dependentId) {
		Enrollee enrollee = null;
		if (enrolleeId != null && (dependentId != null && dependentId.length>0)) {
			enrollee = this.enrolleeRepository.findByEnrolleeId(enrolleeId);
			List<Dependents> list = enrollee.getDependentList().stream().filter(
					d -> !(Arrays.asList(dependentId).contains(d.getDependentId()))).collect(Collectors.toList());
			enrollee.setDependentList(list);
			enrollee = this.enrolleeRepository.save(enrollee);
			
		}
		return enrollee;
	}
	
	/**
	 * Save enrollee.
	 * @param enrollee
	 * @return enrollee or null if no save
	 */
	public Enrollee saveEnrollee(Enrollee enrollee) {
		Enrollee member = null;
		if (enrollee != null && enrollee.getId() == null) {
			member = this.enrolleeRepository.save(enrollee);
		}
		return member;
	}
	
	/**
	 * Remove enrollee by enrollee id.
	 * @param enrolleeId enrollee id.
	 * @return number or rows removed
	 */
	public long removeEnrolleeById(String enrolleeId) {
		return this.enrolleeRepository.deleteByEnrolleeId(enrolleeId);
	}
	
	/**
	 * Update enrollee
	 * @param enrolleeId enrolleeId
	 * @param name name or null
	 * @param activationStatus activationStatus or nuull
	 * @param birthdDate birthDate or null
	 * @param phoneNumber phoneNumber or null
	 * @param ssn ssn or null
	 * @return Enrollee updated enrollee or null/empty if enrollee cannot be found
	 */
	@Modifying(flushAutomatically = true)
	public Enrollee updateEnrollee(String enrolleeId, String name, ActivationStatusCode activationStatus, 
			Integer birthdDate, String phoneNumber, String ssn) {
		Enrollee enrollee = null;
		if (enrolleeId != null) {
			 enrollee = this.enrolleeRepository.findByEnrolleeId(enrolleeId);
			 if (enrollee != null) {
				 if (name != null && name.length()>0) {
					 enrollee.setName(name);
				 }
				 enrollee.setActivationStatus(activationStatus);
				 if(birthdDate != null) {
					 enrollee.setBirthdDate(birthdDate);
				 }
				 if(phoneNumber != null && phoneNumber.length()>0) {
					 enrollee.setPhoneNumber(phoneNumber);
				 }
				 if(ssn != null && ssn.length() == 9) {
					 enrollee.setSsn(ssn);
				 }
				 enrollee = this.enrolleeRepository.save(enrollee);
			 }
		}
		return enrollee;
	}
	 
 
	
 
}
