package com.combatsystem.www.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.combatsystem.www.model.Soldier;
import com.combatsystem.www.model.UAVOperator;

public interface UAVOperatorRepo extends JpaRepository<UAVOperator,Long>{

	UAVOperator findByEmail(String email);
	
	List<UAVOperator> findByStatus(String status);
	
	Optional<UAVOperator> findFirstByEmail(String email);
}
