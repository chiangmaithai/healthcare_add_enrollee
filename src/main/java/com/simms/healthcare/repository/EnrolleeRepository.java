package com.simms.healthcare.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.simms.healthcare.entity.Enrollee;

@Repository
public interface EnrolleeRepository extends CrudRepository<Enrollee, Long>{		
	
	List<Enrollee> findByName(String name);
	
	long deleteByEnrolleeId(String enrolleeId);

}
