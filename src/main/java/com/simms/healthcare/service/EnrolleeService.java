package com.simms.healthcare.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.simms.healthcare.businesslogic.ActivationStatusCode;
import com.simms.healthcare.entity.Dependents;
import com.simms.healthcare.entity.Enrollee;
import com.simms.healthcare.repository.EnrolleeRepository;
import com.simms.healthcare.util.Util;

@Service
public class EnrolleeService {
	
	private static final Logger log = LoggerFactory.getLogger(EnrolleeService.class);

	
	@Autowired
	private EnrolleeRepository enrolleeRepository; 

	/**
	 * Add dependents to enrollee. Ensure enrolleeId is new and dependent isn't already a member,
	 * by checking dependent ssn. If dependent is already a member, then dependent won't be added,
	 * that dependent should be modified
	 * @param enrolleeId enrollee id.
	 * @param newDependentsFromRequest one or more dependents to add
	 * @return the updated enrollee with dependents added or null if no enrollee was found
	 */
	@Modifying(flushAutomatically = true)
	@Transactional (propagation = Propagation.REQUIRES_NEW)
	public Enrollee addDependents(String enrolleeId, List<Dependents> newDependentsFromRequest) {
		Enrollee enrollee = null;
		if (newDependentsFromRequest != null && enrolleeId != null) {
			enrollee = this.enrolleeRepository.findByEnrolleeId(enrolleeId);
			if (enrollee != null) {
				if (enrollee.getDependentList() == null) {
					enrollee.setDependentList(new ArrayList<Dependents>());
				}
				
				List<Dependents> newDependents = newDependentsFromRequest;
				List<String> currentDependentsSSN = new ArrayList<String>();
				//add current dependents ssn to currentDependentsSSN list, so we can filter out
				//any new dependent that is already in the database
				enrollee.getDependentList().stream().forEach(d -> currentDependentsSSN.add(d.getSsn()));
				//if dependent is already a member, then remove dependent from list
				List<Dependents> newDependentsToAdd = newDependents.stream().filter(d -> !currentDependentsSSN.contains(d.getSsn())).collect(Collectors.toList());
				//just verified that all newDependentsToAdd are not current dependents in the database
				//now we can add these dependents to the database
				if (newDependentsToAdd != null) {
					for (Dependents newMember : newDependentsToAdd) {
						Dependents d = new Dependents();
						d.setDependentId(Util.generateNewEnrollmentId());
						d.setName(newMember.getName());
						d.setBirthDate(newMember.getBirthDate());
						d.setSsn(newMember.getSsn());
						enrollee.getDependentList().add(d);
					}
				}
				enrollee = this.enrolleeRepository.save(enrollee);
			}
		}
		return enrollee;
	}
	
	/**
	 * Modify dependents, only depends found will be modified. Only name, ssn and birthdate are modifiable
	 * at this time.
	 * @param enrolleeId enrolleeId
	 * @param dependentList list of dependents to modify, only dependents found will be modified.
	 * @return new Enrollee with modified dependents or null if no enrollee was found
	 */
	@Modifying(flushAutomatically = true)
	@Transactional (propagation = Propagation.REQUIRES_NEW)
	public Enrollee modifyDependents(String enrolleeId, List<Dependents> dependentList) {
		Enrollee enrollee = null;

		if (enrolleeId != null && dependentList != null) {		
			
			enrollee = this.enrolleeRepository.findByEnrolleeId(enrolleeId);
			if (enrollee != null) {								
				// create map of dependentList with key = ssn
				Map<String, Dependents> dMap = dependentList.stream().collect(
						Collectors.toMap(Dependents::getSsn, d -> d));
				
				for (Dependents d : enrollee.getDependentList()) {
					String key = d.getSsn();
					if (dMap.containsKey(key)) {
						d.setSsn(dMap.get(key).getSsn());
						
						if (dMap.get(key).getName()!=null && dMap.get(key).getName().length()>1) {
							d.setName(dMap.get(key).getName());
						}
						
						if (dMap.get(key).getBirthDate() != null) {
							d.setBirthDate(dMap.get(key).getBirthDate());
						}

					}
				}
				
				enrollee = this.enrolleeRepository.save(enrollee);
				
			}
		}
		
		return enrollee;
	}
	
	/**
	 * Remove one or more dependents and return new Enrollee with updated dependents.
	 * @param enrolleeId enrolleeId.
	 * @param dependentId one or more dependent ids to remove.
	 * @return new Enrollee or null/empty if enrollee cannot be found.
	 */
	@Modifying(flushAutomatically = true)
	@Transactional (propagation = Propagation.REQUIRES_NEW)
	public Enrollee removeDependents(String enrolleeId, List<String> dependentId) {
		Enrollee enrollee = null;
		if (enrolleeId != null && (dependentId != null && dependentId.size()>0)) {
			enrollee = this.enrolleeRepository.findByEnrolleeId(enrolleeId);
			if (enrollee != null) {
				//if dependentId isn't in list of dependents to remove, then add to new list of dependents
				List<Dependents> list = enrollee.getDependentList().stream().filter(
						d -> !(dependentId.contains(d.getDependentId()))).collect(Collectors.toList());
				//add new dependent list to enrollee and save
				enrollee.setDependentList(list);
				enrollee = this.enrolleeRepository.save(enrollee);
			}
			
		}
		return enrollee;
	}
	
	/**
	 * Save enrollee. We can save a new enrollee and also the enrollee dependents too.
	 * @param enrollee
	 * @return enrollee or null if no save
	 */
	@Modifying(flushAutomatically = true)
	@Transactional (propagation = Propagation.REQUIRES_NEW)
	public Enrollee saveEnrollee(Enrollee enrolleeRequest) {
		Enrollee member = null;
		if (enrolleeRequest != null) {
			//get data from request and insert enrollee
			Enrollee enrollee = new Enrollee();
			enrollee.setName(enrolleeRequest.getName());
			enrollee.setActivationStatus(enrolleeRequest.getActivationStatus());
			enrollee.setBirthDate(enrolleeRequest.getBirthDate());
			enrollee.setPhoneNumber(enrolleeRequest.getPhoneNumber());
			enrollee.setSsn(enrolleeRequest.getSsn());
			enrollee.setEnrolleeId(Util.generateNewEnrollmentId());

			if (enrolleeRequest.getDependentList() != null && !enrolleeRequest.getDependentList().isEmpty()) {
				enrollee.setDependentList(new ArrayList<Dependents>());
				for (Dependents dependent : enrolleeRequest.getDependentList()) {
					Dependents newDependent = new Dependents();
					newDependent.setBirthDate(dependent.getBirthDate());
					newDependent.setName(dependent.getName());
					newDependent.setSsn(dependent.getSsn());
					newDependent.setDependentId(Util.generateNewEnrollmentId());
					enrollee.getDependentList().add(newDependent);
				}
			}
			
			member = this.enrolleeRepository.save(enrollee);
		}
		return member;
	}
	
	/**
	 * Remove enrollee by enrollee id or group of enrollees
	 * @param enrolleeId enrollee id.
	 * @return number or rows removed
	 */
	@Modifying(flushAutomatically = true)
	@Transactional (propagation = Propagation.REQUIRES_NEW)
	public long removeEnrolleeById(List<String> enrolleeId) {
		long count = 0;
		
		if (enrolleeId != null) {
			for(String eId : enrolleeId) {
				count += this.enrolleeRepository.deleteByEnrolleeId(eId);
			}
		}
		
		return count;
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
	@Transactional (propagation = Propagation.REQUIRES_NEW)
	public Enrollee updateEnrollee(String enrolleeId, String name, ActivationStatusCode activationStatus, 
			Integer birthdDate, String phoneNumber, String ssn) {
		Enrollee enrollee = null;
		if (enrolleeId != null) {
			 enrollee = this.enrolleeRepository.findByEnrolleeId(enrolleeId);			 
			 if (enrollee != null && enrollee.getEnrolleeId() != null) {
				 if (name != null && name.length()>0) {
					 enrollee.setName(name);
				 }
				 enrollee.setActivationStatus(activationStatus);
				 if(birthdDate != null) {
					 enrollee.setBirthDate(birthdDate);
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
