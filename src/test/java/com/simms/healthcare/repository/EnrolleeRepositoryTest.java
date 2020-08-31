package com.simms.healthcare.repository;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Profile;

import com.simms.healthcare.businesslogic.ActivationStatusCode;
import com.simms.healthcare.entity.Dependents;
import com.simms.healthcare.entity.Enrollee;
import com.simms.healthcare.util.Util;

/**
 * By default, tests annotated with @DataJpaTest are transactional and roll backat the end of each 
 * test. They also use an embedded in-memory database (replacing anyexplicit or usually 
 * auto-configured DataSource). The @AutoConfigureTestDatabase annotation can be used to
 * override these settings. 
 * @author thoma
 *
 */
@DataJpaTest
public class EnrolleeRepositoryTest {

	private static final Logger log = LoggerFactory.getLogger(EnrolleeRepositoryTest.class);
	
    @Autowired
    private EnrolleeRepository enrolleeRepository;
    
    /**
     * Saving an enrollee with dependents;
     */
    @Test
    public void whenSavingEnrolleeAndDependents_thenCorrect() {
    	String name = "Brain Cross - " + new Date();
    	String dependentName1 = "Tom Jones " + new Date();
    	String dependentName2 = "Kim Jones " + new Date();    	  
    	
		Dependents d1 = new Dependents();
    	d1.setDependentId(Util.generateNewEnrollmentId());			
		d1.setName(dependentName1);
    	d1.setBirthDate(20200101);
		d1.setSsn("001394823");
    	
		Dependents d2 = new Dependents();
    	d2.setDependentId(Util.generateNewEnrollmentId());
		d2.setName(dependentName2);
    	d2.setBirthDate(20200101);
		d2.setSsn("001394824");
    	
		List<Dependents> list = new ArrayList<Dependents>();
		list.add(d1);
		list.add(d2);
    	
    	Enrollee enrollee = new Enrollee();
    	enrollee.setEnrolleeId(Util.generateNewEnrollmentId());
    	enrollee.setName(name);
    	enrollee.setActivationStatus(ActivationStatusCode.TRUE);
    	enrollee.setBirthDate(19900801);
    	enrollee.setPhoneNumber("8137485545");
    	enrollee.setSsn("087451254");
    	enrollee.setDependentList(list);

    	this.enrolleeRepository.save(enrollee);
    	List<Enrollee> member = this.enrolleeRepository.findByName(name);
    	assertNotNull(member);
    	assertTrue(member.size()==1);
    	assertEquals(name, member.get(0).getName());
    	assertNotNull(member.get(0).getDependentList());
    	assertTrue(member.get(0).getDependentList().size()==2);
    	
    	assertTrue(dependentName1.equals(member.get(0).getDependentList().get(0).getName()) || 
    			dependentName1.equals(member.get(0).getDependentList().get(1).getName()));
    	assertTrue(dependentName2.equals(member.get(0).getDependentList().get(0).getName()) || 
    			dependentName2.equals(member.get(0).getDependentList().get(1).getName()));
    	
    	log.info("********************** " + member.get(0).toString());
    	
    	assertEquals(name, member.get(0).getName());
    }
    
    /**
     * Saving an enrollee;
     */
    @Test
    public void whenSavingEnrollee_thenCorrect() {
    	String name = "Brain Cross - " + new Date();
    	Enrollee enrollee = new Enrollee();
    	enrollee.setEnrolleeId(Util.generateNewEnrollmentId());
    	enrollee.setName(name);
    	enrollee.setActivationStatus(ActivationStatusCode.TRUE);
    	enrollee.setBirthDate(19900801);
    	enrollee.setPhoneNumber("8137485545");
    	enrollee.setSsn("087451254");

    	this.enrolleeRepository.save(enrollee);
    	List<Enrollee> member = this.enrolleeRepository.findByName(name);
    	assertNotNull(member);
    	assertTrue(member.size()>0);

    	log.info("********************** " + member.get(0).toString());
    	
    	assertEquals(name, member.get(0).getName());
    }
	
    /**
     * Ensure enrollee can be deleted.
     */
    @Test
    public void whenSavingEnrolleeThenDeleleEnrollee_thenCorrect() {
    	String name = "Brain Cross - " + new Date();
    	String enrolleeId = Util.generateNewEnrollmentId();    	
    	
    	Enrollee enrollee = new Enrollee();
    	enrollee.setEnrolleeId(enrolleeId);
    	enrollee.setName(name);
    	enrollee.setActivationStatus(ActivationStatusCode.TRUE);
    	enrollee.setBirthDate(19900801);
    	enrollee.setPhoneNumber("8137485545");
    	enrollee.setSsn("087451254");

    	this.enrolleeRepository.save(enrollee);
    	List<Enrollee> member = this.enrolleeRepository.findByName(name);
    	assertNotNull(member);
    	assertTrue(member.size()>0);

    	log.info("********************** " + member.get(0).toString());
    	//assert enrollee was saved
    	assertEquals(name, member.get(0).getName());
    	
    	//delete enrollee
    	long deleteCount = this.enrolleeRepository.deleteByEnrolleeId(enrolleeId);
    	assertTrue(deleteCount == 1);    
    	
    	//try to find enrollee
    	member = this.enrolleeRepository.findByName(name);
    	assertNotNull(member);
    	assertTrue(member.isEmpty());
    }
    
    
    /**
     * Ensure enrollee can be deleted and all of dependents.
     */
    @Test
    public void whenSavingEnrolleeThenDeleleEnrolleeAndDependents_thenCorrect() {
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
		d1.setSsn("001394823");
    	
		Dependents d2 = new Dependents();
    	d2.setDependentId(id2);
		d2.setName(dependentName2);
    	d2.setBirthDate(20200101);
		d2.setSsn("001394824");
		
		Dependents d3 = new Dependents();
    	d3.setDependentId(id3);
		d3.setName(dependentName3);
    	d3.setBirthDate(20200101);
		d3.setSsn("001394825");
		
		Dependents d4 = new Dependents();
    	d4.setDependentId(id4);
		d4.setName(dependentName4);
    	d4.setBirthDate(20200101);
		d4.setSsn("001394826");
		
		List<Dependents> dependentList = new ArrayList<Dependents>();
		dependentList.add(d1);
		dependentList.add(d2);
		dependentList.add(d3);
		dependentList.add(d4);
    	
    	String name = "Brain Cross - " + new Date();
    	String enrolleeId = Util.generateNewEnrollmentId();    	
    	Enrollee enrollee = new Enrollee();
    	enrollee.setEnrolleeId(enrolleeId);
    	enrollee.setName(name);
    	enrollee.setActivationStatus(ActivationStatusCode.TRUE);
    	enrollee.setBirthDate(19900801);
    	enrollee.setPhoneNumber("8137485545");
    	enrollee.setSsn("087451254");
    	enrollee.setDependentList(dependentList);

    	this.enrolleeRepository.save(enrollee);
    	List<Enrollee> member = this.enrolleeRepository.findByName(name);
    	assertNotNull(member);
    	assertTrue(member.size()>0);

    	log.info("********************** " + member.get(0).toString());
    	//assert enrollee was saved
    	assertEquals(name, member.get(0).getName());
    	assertNotNull(member.get(0).getDependentList());
    	assertTrue(member.get(0).getDependentList().size() == 4);
    	
    	//delete enrollee
    	long deleteCount = this.enrolleeRepository.deleteByEnrolleeId(enrolleeId);
    	assertTrue(deleteCount == 1);    
    	
    	//try to find enrollee
    	member = this.enrolleeRepository.findByName(name);
    	assertNotNull(member);
    	assertTrue(member.isEmpty());
    }
    
    

    
	
}
