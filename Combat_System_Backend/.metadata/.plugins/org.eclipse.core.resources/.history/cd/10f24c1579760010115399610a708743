package com.combatsystem.www.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.combatsystem.www.model.Soldier;

public interface SoldierRepo extends JpaRepository<Soldier, Long> {

	
	Soldier findByEmail(String email);
	
	List<Soldier> findByStatus(String status);
	
	Optional<Soldier> findFirstByEmail(String email);


}
