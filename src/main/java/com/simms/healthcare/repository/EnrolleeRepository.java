package com.simms.healthcare.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.simms.healthcare.entity.Enrollee;

@Repository
public interface EnrolleeRepository extends CrudRepository<Enrollee, Long>{		
	
	/**
	 * Find enrollee by name;
	 * @param name enrollee name.
	 * @return enrollee matching name.
	 */
	public List<Enrollee> findByName(String name);
	
	/**
	 * Find enrollee by enrollee id.
	 * @param enrolleeId enrollee id.
	 * @return enrollee
	 */
	public Enrollee findByEnrolleeId(String enrolleeId);
	
	/**
	 * Remove enrollee by enrollee id.
	 * @param enrolleeId enrollee id.
	 * @return number of rows removed.
	 */
	public long deleteByEnrolleeId(String enrolleeId);	
	
}
