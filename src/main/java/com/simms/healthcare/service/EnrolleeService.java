package com.simms.healthcare.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

	/**
	 * Add dependents to enrollee. Ensure enrolleeId is new and dependent isn't already a member,
	 * by checking dependent ssn. If dependent is already a member, then dependent won't be added,
	 * that dependent should be modified
	 * @param enrolleeId enrollee id.
	 * @param dependents one or more dependents to add
	 * @return the updated enrollee with dependents added or null if no enrollee was found
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Enrollee addDependents(String enrolleeId, Dependents... dependents) {
		Enrollee enrollee = null;
		if (dependents != null && enrolleeId != null) {
			enrollee = this.enrolleeRepository.findByEnrolleeId(enrolleeId);
			if (enrollee != null && enrollee.getDependentList() != null) {
				List<Dependents> newDependents = Arrays.asList(dependents);
				List<String> currentSSNList = new ArrayList<String>();
				enrollee.getDependentList().stream().forEach(d -> currentSSNList.add(d.getSsn()));
				//if dependent is already a member, then remove dependent from list
				List<Dependents> newDependentsToAdd = newDependents.stream().filter(d -> !currentSSNList.contains(d.getSsn())).collect(Collectors.toList());
				if (newDependentsToAdd != null) {
					enrollee.getDependentList().addAll(newDependentsToAdd);
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
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Enrollee modifyDependents(String enrolleeId, Dependents... dependentList) {
		Enrollee enrollee = null;

		if (enrolleeId != null && dependentList != null) {		
			
			enrollee = this.enrolleeRepository.findByEnrolleeId(enrolleeId);
			if (enrollee != null) {								
				// create map of dependentList with key = ssn
				Map<String, Dependents> dMap = Arrays.asList(dependentList).stream().collect(
						Collectors.toMap(Dependents::getSsn, d -> d));
				
				for (Dependents d : enrollee.getDependentList()) {
					String key = d.getSsn();
					if (dMap.containsKey(key)) {
						d.setSsn(dMap.get(key).getSsn());
						
						if (dMap.get(key).getName()!=null && dMap.get(key).getName().length()>1) {
							d.setName(dMap.get(key).getName());
						}
						
						if (dMap.get(key).getBirthdDate() != null) {
							d.setBirthdDate(dMap.get(key).getBirthdDate());
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
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Enrollee removeDependents(String enrolleeId, String... dependentId) {
		Enrollee enrollee = null;
		if (enrolleeId != null && (dependentId != null && dependentId.length>0)) {
			enrollee = this.enrolleeRepository.findByEnrolleeId(enrolleeId);
			if (enrollee != null) {
				//if dependentId isn't in list of dependents to remove, then add to new list of dependents
				List<Dependents> list = enrollee.getDependentList().stream().filter(
						d -> !(Arrays.asList(dependentId).contains(d.getDependentId()))).collect(Collectors.toList());
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
	public Enrollee saveEnrollee(Enrollee enrollee) {
		Enrollee member = null;
		if (enrollee != null && enrollee.getId() == null) {
			member = this.enrolleeRepository.save(enrollee);
		}
		return member;
	}
	
	/**
	 * Remove enrollee by enrollee id or group of enrollees
	 * @param enrolleeId enrollee id.
	 * @return number or rows removed
	 */
	public long removeEnrolleeById(String... enrolleeId) {
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
