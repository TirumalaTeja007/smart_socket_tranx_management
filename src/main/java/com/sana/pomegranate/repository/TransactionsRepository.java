package com.sana.pomegranate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sana.pomegranate.enums.SessionStatusEnum;
import com.sana.pomegranate.model.Transactions;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Integer> {

	Optional<List<Transactions>> findByUserNameAndSessionId(String userName, String sessionId);
	
	Optional<List<Transactions>> findByUserName(String userName);

	Optional<List<Transactions>> findByUserNameAndSessionStatusEnum(String userName, SessionStatusEnum sessionStatusEnum);  
}  
  