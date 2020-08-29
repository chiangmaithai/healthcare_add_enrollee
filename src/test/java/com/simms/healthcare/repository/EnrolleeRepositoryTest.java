package com.simms.healthcare.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

import com.simms.healthcare.entity.Dependents;
import com.simms.healthcare.entity.Enrollee;
import com.simms.healthcare.service.ActivationStatusCode;
import com.simms.healthcare.util.Util;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
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
    	d1.setDependentId(Util.getNewId());			
		d1.setName(dependentName1);
    	d1.setBirthdDate(20200101);
		d1.setSsn("001394823");
    	
		Dependents d2 = new Dependents();
    	d2.setDependentId(Util.getNewId());
		d2.setName(dependentName2);
    	d2.setBirthdDate(20200101);
		d2.setSsn("001394823");
    	
		List<Dependents> list = new ArrayList<Dependents>();
		list.add(d1);
		list.add(d2);
    	
    	Enrollee enrollee = new Enrollee();
    	enrollee.setEnrolleeId(Util.getNewId());
    	enrollee.setName(name);
    	enrollee.setActivationStatus(ActivationStatusCode.TRUE);
    	enrollee.setBirthdDate(19900801);
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
    	enrollee.setEnrolleeId(Util.getNewId());
    	enrollee.setName(name);
    	enrollee.setActivationStatus(ActivationStatusCode.TRUE);
    	enrollee.setBirthdDate(19900801);
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
    	String enrolleeId = Util.getNewId();    	
    	
    	Enrollee enrollee = new Enrollee();
    	enrollee.setEnrolleeId(enrolleeId);
    	enrollee.setName(name);
    	enrollee.setActivationStatus(ActivationStatusCode.TRUE);
    	enrollee.setBirthdDate(19900801);
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
     * Ensure one dependent can be deleted for a list of dependents.
     */
    @Test
    public void whenDeletingDepdenent_thenCorrect() {
    	String name = "Brain Cross - " + new Date();
    	String enrolleeId = Util.getNewId();    	
    	
    	Enrollee enrollee = new Enrollee();
    	enrollee.setEnrolleeId(enrolleeId);
    	enrollee.setName(name);
    	enrollee.setActivationStatus(ActivationStatusCode.TRUE);
    	enrollee.setBirthdDate(19900801);
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
	
}
