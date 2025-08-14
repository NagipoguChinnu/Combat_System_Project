package com.combatsystem.www.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.combatsystem.www.model.SoldierEncryptedMessage;

@Repository
public interface SoldierEncryptedRepository extends JpaRepository<SoldierEncryptedMessage, Long>{

	List<SoldierEncryptedMessage> findBySoldierOperatorId(Long id);
}
