package com.simms.healthcare.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;

import com.simms.healthcare.entity.Dependents;
import com.simms.healthcare.entity.Enrollee;
import com.simms.healthcare.repository.EnrolleeRepository;
import com.simms.healthcare.util.Util;

@SpringBootTest
//@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
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
   	
    	String id1 = Util.getNewId();
    	
		Dependents d1 = new Dependents();
    	d1.setDependentId(id1);			
		d1.setName(dependentName1);
    	d1.setBirthdDate(20200101);
		d1.setSsn("001394823");
    	
		List<Dependents> list = new ArrayList<Dependents>();
		list.add(d1);
    	
    	Enrollee enrollee = new Enrollee();
    	String enrolleeId = Util.getNewId();
    	enrollee.setEnrolleeId(enrolleeId);
    	enrollee.setName(name);
    	enrollee.setActivationStatus(ActivationStatusCode.TRUE);
    	enrollee.setBirthdDate(19900801);
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
    	String id = Util.getNewId();
    	String  newSSN = "777777775";
		Dependents newD = new Dependents();
		newD.setDependentId(id);			
		newD.setName(newName);
		newD.setBirthdDate(20201212);
		newD.setSsn(newSSN);
		
		Enrollee updatedEnrollee = this.enrolleeService.addDependents(enrolleeId, newD);
		
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
   	
    	String id1 = Util.getNewId();
    	
		Dependents d1 = new Dependents();
    	d1.setDependentId(id1);			
		d1.setName(dependentName1);
    	d1.setBirthdDate(20200101);
		d1.setSsn("001394823");
    	
		List<Dependents> list = new ArrayList<Dependents>();
		list.add(d1);
    	
    	Enrollee enrollee = new Enrollee();
    	String enrolleeId = Util.getNewId();
    	enrollee.setEnrolleeId(enrolleeId);
    	enrollee.setName(name);
    	enrollee.setActivationStatus(ActivationStatusCode.TRUE);
    	enrollee.setBirthdDate(19900801);
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
    	String id = Util.getNewId();
    	String  newSSN = "001394823"; //duplicate ssn
		Dependents newD = new Dependents();
		newD.setDependentId(id);			
		newD.setName(newName);
		newD.setBirthdDate(20201212);
		newD.setSsn(newSSN);
		
		Enrollee updatedEnrollee = this.enrolleeService.addDependents(enrolleeId, newD);
		
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
   	
    	String id1 = Util.getNewId();
    	
		Dependents d1 = new Dependents();
    	d1.setDependentId(id1);			
		d1.setName(dependentName1);
    	d1.setBirthdDate(20200101);
		d1.setSsn("001394823");
    	
		List<Dependents> list = new ArrayList<Dependents>();
		list.add(d1);
    	
    	Enrollee enrollee = new Enrollee();
    	String enrolleeId = Util.getNewId();
    	enrollee.setEnrolleeId(enrolleeId);
    	enrollee.setName(name);
    	enrollee.setActivationStatus(ActivationStatusCode.TRUE);
    	enrollee.setBirthdDate(19900801);
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
    	String id = Util.getNewId();
    	String  newSSN = "777777775";
		Dependents newD = new Dependents();
		newD.setDependentId(id);			
		newD.setName(newName);
		newD.setBirthdDate(20201212);
		newD.setSsn(newSSN);
		
		Enrollee updatedEnrollee = this.enrolleeService.addDependents(enrolleeId, newD);
				
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

		Enrollee modifiedEnrollee = this.enrolleeService.modifyDependents(enrolleeId, d2);
		
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
   	
    	String id1 = Util.getNewId();
    	String id2 = Util.getNewId();
    	String id3 = Util.getNewId();
    	
		Dependents d1 = new Dependents();
    	d1.setDependentId(id1);			
		d1.setName(dependentName1 + " - 1");
    	d1.setBirthdDate(20200101);
		d1.setSsn("101394823");
		
		Dependents d2 = new Dependents();
    	d2.setDependentId(id2);			
		d2.setName(dependentName1 + " - 2");
    	d2.setBirthdDate(20200101);
		d2.setSsn("201394823");
		
		Dependents d3 = new Dependents();
    	d3.setDependentId(id3);			
		d3.setName(dependentName1 + " - 3");
    	d3.setBirthdDate(20200101);
		d3.setSsn("301394823");
    	
		List<Dependents> list = new ArrayList<Dependents>();
		list.add(d3);
		list.add(d1);
		list.add(d2);
    	
    	Enrollee enrollee = new Enrollee();
    	String enrolleeId = Util.getNewId();
    	enrollee.setEnrolleeId(enrolleeId);
    	enrollee.setName(name);
    	enrollee.setActivationStatus(ActivationStatusCode.TRUE);
    	enrollee.setBirthdDate(19900801);
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
    	String id = Util.getNewId();
    	String  newSSN = "777777775";
		Dependents newD = new Dependents();
		newD.setDependentId(id);			
		newD.setName(newName);
		newD.setBirthdDate(20201212);
		newD.setSsn(newSSN);
		
		Enrollee updatedEnrollee = this.enrolleeService.addDependents(enrolleeId, newD);
				
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
		Enrollee modifiedEnrollee = this.enrolleeService.removeDependents(
				enrolleeId, KarenDependentId.get(0).getDependentId());
		
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
   	
    	String id1 = Util.getNewId();
    	
		Dependents d1 = new Dependents();
    	d1.setDependentId(id1);			
		d1.setName(dependentName1);
    	d1.setBirthdDate(20200101);
		d1.setSsn("001394823");
    	
		List<Dependents> list = new ArrayList<Dependents>();
		list.add(d1);
    	
    	Enrollee enrollee = new Enrollee();
    	String enrolleeId = Util.getNewId();
    	enrollee.setEnrolleeId(enrolleeId);
    	enrollee.setName(name);
    	enrollee.setActivationStatus(ActivationStatusCode.TRUE);
    	enrollee.setBirthdDate(19900801);
    	enrollee.setPhoneNumber("8137485545");
    	enrollee.setSsn("087451254");
    	enrollee.setDependentList(list);

    	this.enrolleeRepository.save(enrollee);
    	Enrollee member = this.enrolleeRepository.findByEnrolleeId(enrolleeId);

    	//verify dependent exists - id3   	
    	assertNotNull(member);
    	assertNotNull(member.getDependentList());
    	assertTrue(!member.getDependentList().isEmpty());
    	assertTrue(member.getDependentList().stream().anyMatch(dependent -> id1.equals(dependent.getDependentId())));
    	
    	// modify
    	String newSSn = "111111111";
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
    	assertEquals(newBirth, updatedEnrollee.getBirthdDate());
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

		String id1 = Util.getNewId();
		String id2 = Util.getNewId();
		String id3 = Util.getNewId();
		String id4 = Util.getNewId();

		Dependents d1 = new Dependents();
		d1.setDependentId(id1);
		d1.setName(dependentName1);
		d1.setBirthdDate(20200101);
		d1.setSsn("001394823");

		Dependents d2 = new Dependents();
		d2.setDependentId(id2);
		d2.setName(dependentName2);
		d2.setBirthdDate(20200101);
		d2.setSsn("001394824");

		Dependents d3 = new Dependents();
		d3.setDependentId(id3);
		d3.setName(dependentName3);
		d3.setBirthdDate(20200101);
		d3.setSsn("001394825");

		Dependents d4 = new Dependents();
		d4.setDependentId(id4);
		d4.setName(dependentName4);
		d4.setBirthdDate(20200101);
		d4.setSsn("001394826");

		List<Dependents> list = new ArrayList<Dependents>();
		list.add(d1);
		list.add(d2);
		list.add(d3);
		list.add(d4);

		Enrollee enrollee = new Enrollee();
		String enrolleeId = Util.getNewId();
		enrollee.setEnrolleeId(enrolleeId);
		enrollee.setName(name);
		enrollee.setActivationStatus(ActivationStatusCode.TRUE);
		enrollee.setBirthdDate(19900801);
		enrollee.setPhoneNumber("8137485545");
		enrollee.setSsn("087451254");
		enrollee.setDependentList(list);

		this.enrolleeRepository.save(enrollee);
		Enrollee member = this.enrolleeRepository.findByEnrolleeId(enrolleeId);

		// verify dependent exists - id3
		assertNotNull(member);
		assertNotNull(member.getDependentList());
		assertTrue(!member.getDependentList().isEmpty());
		assertTrue(member.getDependentList().stream().anyMatch(dependent -> id3.equals(dependent.getDependentId())));

		// delete dependent with
		Enrollee updatedEnrollee = this.enrolleeService.removeDependents(enrolleeId, id3);

		// verify dependent no longer exists - id3
		assertNotNull(updatedEnrollee);
		assertNotNull(updatedEnrollee.getDependentList());
		assertTrue(!updatedEnrollee.getDependentList().isEmpty());
		assertFalse(updatedEnrollee.getDependentList().stream()
				.anyMatch(dependent -> id3.equals(dependent.getDependentId())));

		// delete 2 more dependents - id1 and id4
		updatedEnrollee = this.enrolleeService.removeDependents(enrolleeId, id1, id4);
		assertNotNull(updatedEnrollee.getDependentList());
		assertTrue(updatedEnrollee.getDependentList().size() == 1);
		assertTrue(updatedEnrollee.getDependentList().get(0).getDependentId().equals(id2));
	}

}
