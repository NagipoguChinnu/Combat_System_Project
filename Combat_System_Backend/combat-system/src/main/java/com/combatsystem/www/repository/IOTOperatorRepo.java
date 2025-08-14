package com.combatsystem.www.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.combatsystem.www.model.IOTOperator;

public interface IOTOperatorRepo extends JpaRepository<IOTOperator,Long>{

	IOTOperator findByEmail(String email);
	
	List<IOTOperator> findByStatus(String status);
	
	Optional<IOTOperator> findFirstByEmail(String email);
}
