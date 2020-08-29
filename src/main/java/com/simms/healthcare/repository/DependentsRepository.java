package com.simms.healthcare.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.simms.healthcare.entity.Dependents;

@Repository
public interface DependentsRepository extends CrudRepository<Dependents, Long>{		

}
