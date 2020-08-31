package com.simms.healthcare.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.simms.healthcare.businesslogic.ActivationStatusCode;
import com.simms.healthcare.entity.Dependents;
import com.simms.healthcare.entity.Enrollee;
import com.simms.healthcare.repository.EnrolleeRepository;
import com.simms.healthcare.util.Util;

@SpringBootTest
@ActiveProfiles("test")

//@AutoConfigureTestDatabase(replace = Replace.NONE)
public class EnrolleeServiceTest {

	private static final Logger log = LoggerFactory.getLogger(EnrolleeServiceTest.class);

	@Autowired
	private EnrolleeService enrolleeService;

	@Autowired
	private EnrolleeRepository enrolleeRepository;

	
	/**
	 * Ensure Service can add dependents. 
	 */
	@Test
	public void whenAddDependents_thenCorrect() {
    	String name = "Susan Friday - " + new Date();
    	String dependentName1 = "Phillip Carribean " + new Date();
   	
    	String id1 = Util.generateNewEnrollmentId();
    	
		Dependents d1 = new Dependents();
    	d1.setDependentId(id1);			
		d1.setName(dependentName1);
    	d1.setBirthDate(20200101);
		d1.setSsn("201394823");
    	
		List<Dependents> list = new ArrayList<Dependents>();
		list.add(d1);
    	
    	Enrollee enrollee = new Enrollee();
    	String enrolleeId = Util.generateNewEnrollmentId();
    	enrollee.setEnrolleeId(enrolleeId);
    	enrollee.setName(name);
    	enrollee.setActivationStatus(ActivationStatusCode.TRUE);
    	enrollee.setBirthDate(19900801);
    	enrollee.setPhoneNumber("8137485545");
    	enrollee.setSsn("287451254");
    	enrollee.setDependentList(list);

    	this.enrolleeRepository.save(enrollee);
    	Enrollee member = this.enrolleeRepository.findByEnrolleeId(enrolleeId);
    	
    	//ensure enrollee exists
    	assertNotNull(member);
    	assertNotNull(member.getDependentList());
    	assertTrue(!member.getDependentList().isEmpty());
    	assertTrue(member.getDependentList().stream().anyMatch(dependent -> id1.equals(dependent.getDependentId())));
    	
    	// add new dependent
    	String newName = "Karen Fish - " + new Date();   
    	String id = Util.generateNewEnrollmentId();
    	String  newSSN = "377777775";
		Dependents newD = new Dependents();
		newD.setDependentId(id);			
		newD.setName(newName);
		newD.setBirthDate(20201212);
		newD.setSsn(newSSN);
		
		Dependents[] array = {newD};
		Enrollee updatedEnrollee = this.enrolleeService.addDependents(enrolleeId, Arrays.asList(array));
		
		//search for new dependent
    	assertNotNull(updatedEnrollee.getDependentList());
    	assertTrue(!updatedEnrollee.getDependentList().isEmpty());
    	assertTrue(updatedEnrollee.getDependentList().stream().anyMatch(dependent -> newSSN.equals(dependent.getSsn()
    			)));
    	
	}			
	
	/**
	 * Ensure Service can add dependents but duplicate dependents will not be added.
	 */
	@Test
	public void whenAddDuplicateDependents_thenFail() {
    	String name = "Susan Friday - " + new Date();
    	String dependentName1 = "Phillip Carribean " + new Date();
   	
    	String id1 = Util.generateNewEnrollmentId();
    	
		Dependents d1 = new Dependents();
    	d1.setDependentId(id1);			
		d1.setName(dependentName1);
    	d1.setBirthDate(20200101);
		d1.setSsn("001394823");
    	
		List<Dependents> list = new ArrayList<Dependents>();
		list.add(d1);
    	
    	Enrollee enrollee = new Enrollee();
    	String enrolleeId = Util.generateNewEnrollmentId();
    	enrollee.setEnrolleeId(enrolleeId);
    	enrollee.setName(name);
    	enrollee.setActivationStatus(ActivationStatusCode.TRUE);
    	enrollee.setBirthDate(19900801);
    	enrollee.setPhoneNumber("8137485545");
    	enrollee.setSsn("087451254");
    	enrollee.setDependentList(list);

    	this.enrolleeRepository.save(enrollee);
    	Enrollee member = this.enrolleeRepository.findByEnrolleeId(enrolleeId);
    	
    	//ensure enrollee exists
    	assertNotNull(member);
    	assertNotNull(member.getDependentList());
    	assertTrue(!member.getDependentList().isEmpty());
    	assertTrue(member.getDependentList().stream().anyMatch(dependent -> id1.equals(dependent.getDependentId())));
    	
    	// add new dependent
    	String newName = "Karen Fish - " + new Date();   
    	String id = Util.generateNewEnrollmentId();
    	String  newSSN = "001394823"; //duplicate ssn
		Dependents newD = new Dependents();
		newD.setDependentId(id);			
		newD.setName(newName);
		newD.setBirthDate(20201212);
		newD.setSsn(newSSN);
		
		Dependents[] array = {newD};
		Enrollee updatedEnrollee = this.enrolleeService.addDependents(enrolleeId, Arrays.asList(array));
		
		//search for new dependent, there should only be 1 dependent
    	assertNotNull(updatedEnrollee.getDependentList());
    	assertTrue(!updatedEnrollee.getDependentList().isEmpty());
    	assertTrue(updatedEnrollee.getDependentList().size() == 1);
    	assertTrue(updatedEnrollee.getDependentList().stream().anyMatch(dependent -> newSSN.equals(dependent.getSsn()
    			)));
    	
	}
	
	/**
	 * Test Service can modify dependent.
	 */
	@Test
	public void whenModifyingDependent_thenCorrect() {
		
    	String name = "Susan Friday - " + new Date();
    	String dependentName1 = "Phillip Carribean " + new Date();
   	
    	String id1 = Util.generateNewEnrollmentId();
    	
		Dependents d1 = new Dependents();
    	d1.setDependentId(id1);			
		d1.setName(dependentName1);
    	d1.setBirthDate(20200101);
		d1.setSsn("001664823");
    	
		List<Dependents> list = new ArrayList<Dependents>();
		list.add(d1);
    	
    	Enrollee enrollee = new Enrollee();
    	String enrolleeId = Util.generateNewEnrollmentId();
    	enrollee.setEnrolleeId(enrolleeId);
    	enrollee.setName(name);
    	enrollee.setActivationStatus(ActivationStatusCode.TRUE);
    	enrollee.setBirthDate(19900801);
    	enrollee.setPhoneNumber("8137485545");
    	enrollee.setSsn("087555254");
    	enrollee.setDependentList(list);

    	this.enrolleeRepository.save(enrollee);
    	Enrollee member = this.enrolleeRepository.findByEnrolleeId(enrolleeId);
    	
    	//ensure enrollee exists
    	assertNotNull(member);
    	assertNotNull(member.getDependentList());
    	assertTrue(!member.getDependentList().isEmpty());
    	assertTrue(member.getDependentList().stream().anyMatch(dependent -> id1.equals(dependent.getDependentId())));
    	
    	// add new dependent
    	String newName = "Karen Fish - " + new Date();   
    	String id = Util.generateNewEnrollmentId();
    	String  newSSN = "776677775";
		Dependents newD = new Dependents();
		newD.setDependentId(id);			
		newD.setName(newName);
		newD.setBirthDate(20201212);
		newD.setSsn(newSSN);
		
		Dependents[] array = {newD};
		Enrollee updatedEnrollee = this.enrolleeService.addDependents(enrolleeId, Arrays.asList(array));
				
		//search for new dependent
    	assertNotNull(updatedEnrollee.getDependentList());
    	assertTrue(!updatedEnrollee.getDependentList().isEmpty());
    	assertTrue(updatedEnrollee.getDependentList().stream().anyMatch(dependent -> newSSN.equals(dependent.getSsn()
    			)));
    	assertTrue(updatedEnrollee.getDependentList().stream().anyMatch(dependent -> newName.equals(dependent.getName()
    			)));
    	
		
    	//modify the dependent Karen to Donald
		Dependents d2 = new Dependents();
    	d2.setDependentId(id1);	
    	d2.setSsn(newSSN);
		d2.setName("Donald Truman");

		Dependents[] array2 = {d2};
		Enrollee modifiedEnrollee = this.enrolleeService.modifyDependents(enrolleeId, Arrays.asList(array2));
		
		//search for modified dependent
    	assertNotNull(modifiedEnrollee.getDependentList());
    	assertTrue(!modifiedEnrollee.getDependentList().isEmpty());
    	assertTrue(modifiedEnrollee.getDependentList().stream().anyMatch(
    			dependent -> "Donald Truman".equals(dependent.getName())));
	}
	
	
	/**
	 * Test Service can remove dependent.
	 */
	@Test
	public void whenRemvoingDependent_thenCorrect() {
		
    	String name = "Susan Friday - " + new Date();
    	String dependentName1 = "Phillip Carribean " + new Date();
   	
    	String id1 = Util.generateNewEnrollmentId();
    	String id2 = Util.generateNewEnrollmentId();
    	String id3 = Util.generateNewEnrollmentId();
    	
		Dependents d1 = new Dependents();
    	d1.setDependentId(id1);			
		d1.setName(dependentName1 + " - 1");
    	d1.setBirthDate(20200101);
		d1.setSsn("101377723");
		
		Dependents d2 = new Dependents();
    	d2.setDependentId(id2);			
		d2.setName(dependentName1 + " - 2");
    	d2.setBirthDate(20200101);
		d2.setSsn("205594823");
		
		Dependents d3 = new Dependents();
    	d3.setDependentId(id3);			
		d3.setName(dependentName1 + " - 3");
    	d3.setBirthDate(20200101);
		d3.setSsn("301377823");
    	
		List<Dependents> list = new ArrayList<Dependents>();
		list.add(d3);
		list.add(d1);
		list.add(d2);
    	
    	Enrollee enrollee = new Enrollee();
    	String enrolleeId = Util.generateNewEnrollmentId();
    	enrollee.setEnrolleeId(enrolleeId);
    	enrollee.setName(name);
    	enrollee.setActivationStatus(ActivationStatusCode.TRUE);
    	enrollee.setBirthDate(19900801);
    	enrollee.setPhoneNumber("8137485545");
    	enrollee.setSsn("087345254");
    	enrollee.setDependentList(list);

    	this.enrolleeRepository.save(enrollee);
    	Enrollee member = this.enrolleeRepository.findByEnrolleeId(enrolleeId);
    	
    	//ensure enrollee exists
    	assertNotNull(member);
    	assertNotNull(member.getDependentList());
    	assertTrue(!member.getDependentList().isEmpty());
    	assertTrue(member.getDependentList().stream().anyMatch(dependent -> id1.equals(dependent.getDependentId())));
    	
    	// add new dependent
    	String newName = "Karen Fish - " + new Date();   
    	String id = Util.generateNewEnrollmentId();
    	String  newSSN = "777333775";
		Dependents newD = new Dependents();
		newD.setDependentId(id);			
		newD.setName(newName);
		newD.setBirthDate(20201212);
		newD.setSsn(newSSN);
		
		Dependents[] array = { newD};
		Enrollee updatedEnrollee = this.enrolleeService.addDependents(enrolleeId, Arrays.asList(array));
				
		//search for new dependent
    	assertNotNull(updatedEnrollee.getDependentList());
    	assertTrue(!updatedEnrollee.getDependentList().isEmpty());
    	assertTrue(updatedEnrollee.getDependentList().stream().anyMatch(dependent -> newSSN.equals(dependent.getSsn()
    			)));
    	assertTrue(updatedEnrollee.getDependentList().stream().anyMatch(dependent -> newName.equals(dependent.getName()
    			)));
    	
    	List<Dependents> KarenDependentId = updatedEnrollee.getDependentList().stream().
    			filter(d -> d.getName().startsWith("Karen")).collect(Collectors.toList());
		assertNotNull(KarenDependentId);
		assertTrue(KarenDependentId.size()==1);
    	
    	//now remove dependent
		String[] array2 = {KarenDependentId.get(0).getDependentId()};
		Enrollee modifiedEnrollee = this.enrolleeService.removeDependents(
				enrolleeId, Arrays.asList(array2));
		
		//search for modified dependent
    	assertNotNull(modifiedEnrollee.getDependentList());
    	assertTrue(!modifiedEnrollee.getDependentList().isEmpty());
    	assertFalse(modifiedEnrollee.getDependentList().stream().anyMatch(
    			dependent -> dependent.getName().startsWith("Karen")));
	}
	
	/**
     * Ensure a enrollee can be modified.
     */
    @Test
    public void whenModifyingEnrollee_thenCorrect() {
    	String name = "Brain Cross - " + new Date();
    	String dependentName1 = "Tom Jones1 " + new Date();
   	
    	String id1 = Util.generateNewEnrollmentId();
    	
		Dependents d1 = new Dependents();
    	d1.setDependentId(id1);			
		d1.setName(dependentName1);
    	d1.setBirthDate(20200101);
		d1.setSsn("091394823");
    	
		List<Dependents> list = new ArrayList<Dependents>();
		list.add(d1);
    	
    	Enrollee enrollee = new Enrollee();
    	String enrolleeId = Util.generateNewEnrollmentId();
    	enrollee.setEnrolleeId(enrolleeId);
    	enrollee.setName(name);
    	enrollee.setActivationStatus(ActivationStatusCode.TRUE);
    	enrollee.setBirthDate(19900801);
    	enrollee.setPhoneNumber("8137485545");
    	enrollee.setSsn("089451254");
    	enrollee.setDependentList(list);

    	this.enrolleeRepository.save(enrollee);
    	Enrollee member = this.enrolleeRepository.findByEnrolleeId(enrolleeId);

    	//verify dependent exists - id3   	
    	assertNotNull(member);
    	assertNotNull(member.getDependentList());
    	assertTrue(!member.getDependentList().isEmpty());
    	assertTrue(member.getDependentList().stream().anyMatch(dependent -> id1.equals(dependent.getDependentId())));
    	
    	// modify
    	String newSSn = "111110111";
    	String newName = "Tony Jones";
    	ActivationStatusCode newStatus = ActivationStatusCode.FALSE;
    	Integer newBirth = Integer.valueOf(20200901);
    	String newPhone = "8137771234";    	
    	
    	Enrollee updatedEnrollee = this.enrolleeService.updateEnrollee(enrolleeId, newName, 
    			newStatus, newBirth, newPhone, newSSn);
 
    	assertNotNull(updatedEnrollee);
    	assertEquals(newSSn, updatedEnrollee.getSsn());
    	assertEquals(newName, updatedEnrollee.getName());
    	assertEquals(newStatus.getCode(), updatedEnrollee.getActivationStatus().getCode());
    	assertEquals(newBirth, updatedEnrollee.getBirthDate());
    	assertEquals(newPhone, updatedEnrollee.getPhoneNumber());
    	assertNotEquals(name, updatedEnrollee.getName());
    	
    }

	/**
	 * Ensure a dependent can be removed for a list of dependents.
	 */
	@Test
	public void whenRemovingOneOfManyDependents_thenCorrect() {
		String name = "Brain Cross - " + new Date();
		String dependentName1 = "Tom Jones1 " + new Date();
		String dependentName2 = "Kim Jones2 " + new Date();
		String dependentName3 = "Kim Jones3 " + new Date();
		String dependentName4 = "Kim Jones4 " + new Date();

		String id1 = Util.generateNewEnrollmentId();
		String id2 = Util.generateNewEnrollmentId();
		String id3 = Util.generateNewEnrollmentId();
		String id4 = Util.generateNewEnrollmentId();

		Dependents d1 = new Dependents();
		d1.setDependentId(id1);
		d1.setName(dependentName1);
		d1.setBirthDate(20200101);
		d1.setSsn("021324823");

		Dependents d2 = new Dependents();
		d2.setDependentId(id2);
		d2.setName(dependentName2);
		d2.setBirthDate(20200101);
		d2.setSsn("031334824");

		Dependents d3 = new Dependents();
		d3.setDependentId(id3);
		d3.setName(dependentName3);
		d3.setBirthDate(20200101);
		d3.setSsn("041394825");

		Dependents d4 = new Dependents();
		d4.setDependentId(id4);
		d4.setName(dependentName4);
		d4.setBirthDate(20200101);
		d4.setSsn("051394526");

		List<Dependents> list = new ArrayList<Dependents>();
		list.add(d1);
		list.add(d2);
		list.add(d3);
		list.add(d4);

		Enrollee enrollee = new Enrollee();
		String enrolleeId = Util.generateNewEnrollmentId();
		enrollee.setEnrolleeId(enrolleeId);
		enrollee.setName(name);
		enrollee.setActivationStatus(ActivationStatusCode.TRUE);
		enrollee.setBirthDate(19900801);
		enrollee.setPhoneNumber("8137485545");
		enrollee.setSsn("087651254");
		enrollee.setDependentList(list);

		this.enrolleeRepository.save(enrollee);
		Enrollee member = this.enrolleeRepository.findByEnrolleeId(enrolleeId);

		// verify dependent exists - id3
		assertNotNull(member);
		assertNotNull(member.getDependentList());
		assertTrue(!member.getDependentList().isEmpty());
		assertTrue(member.getDependentList().stream().anyMatch(dependent -> id3.equals(dependent.getDependentId())));

		// delete dependent with
		String[] array = {id3};
		Enrollee updatedEnrollee = this.enrolleeService.removeDependents(enrolleeId, Arrays.asList(array));

		// verify dependent no longer exists - id3
		assertNotNull(updatedEnrollee);
		assertNotNull(updatedEnrollee.getDependentList());
		assertTrue(!updatedEnrollee.getDependentList().isEmpty());
		assertFalse(updatedEnrollee.getDependentList().stream()
				.anyMatch(dependent -> id3.equals(dependent.getDependentId())));

		// delete 2 more dependents - id1 and id4
		String[] array2 = {id1, id4};
		updatedEnrollee = this.enrolleeService.removeDependents(enrolleeId, Arrays.asList(array2));
		assertNotNull(updatedEnrollee.getDependentList());
		assertTrue(updatedEnrollee.getDependentList().size() == 1);
		assertTrue(updatedEnrollee.getDependentList().get(0).getDependentId().equals(id2));
	}

}
