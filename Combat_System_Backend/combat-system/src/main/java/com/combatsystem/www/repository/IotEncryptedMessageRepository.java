package com.combatsystem.www.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.combatsystem.www.model.IotEncryptedMessage;

@Repository
public interface IotEncryptedMessageRepository extends JpaRepository<IotEncryptedMessage, Long>{

	List<IotEncryptedMessage> findByIotOperatorId(Long id);
}
