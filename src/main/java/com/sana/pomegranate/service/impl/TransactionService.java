package com.sana.pomegranate.service.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sana.pomegranate.model.User;
import com.sana.pomegranate.enums.SessionStatusEnum;
import com.sana.pomegranate.exception.ResourceRegistrationException;
import com.sana.pomegranate.exception.ResourceUpdationException;
import com.sana.pomegranate.model.Transactions;
import com.sana.pomegranate.repository.TransactionsRepository;
import com.sana.pomegranate.service.ITransactionService;

@Service
public class TransactionService implements ITransactionService {
	
	private static Logger logger = LogManager.getLogger(TransactionService.class);
	
	@Autowired
	TransactionsRepository transRepo;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	RestTemplate restTemplate;

	@Override
	public Optional<Transactions> addTransaction(@Valid Transactions transactions) {
		
		Transactions newTransactions = new Transactions();
		try {
			transactions.setUserName(transactions.getUserName());
			transactions.setCreatedTime(new Date());
			
			
			newTransactions = transRepo.save(transactions);
			return Optional.ofNullable(newTransactions);
			
			
		}catch(Exception e) {
			throw new ResourceRegistrationException("Transactions", "Failed to Create Transactions.");
		}
	}

	@Override
	@Transactional
	public Optional<Transactions> updateTransaction(@Valid Transactions transactions) {
		Transactions newTransactions = new Transactions();
		try {
			transactions.setUserName(transactions.getUserName());
			transactions.setUpdatedTime(new Date());
			
			
			newTransactions = transRepo.save(transactions);
			return Optional.ofNullable(newTransactions);
			
		}catch(Exception e) {
			throw new ResourceUpdationException("Transactions", "Failed to Update the Transaction.");
		}
	}
	
	
	@Override
	public Optional<List<Transactions>> getAllTransactions() {
		return Optional.ofNullable(transRepo.findAll());
	}

	
	@Override
	public User getUser(String userName) throws URISyntaxException {
		User user = null;
		String api = "http://smartsocket.axonifytech.com/apple/api/user/userName/"+userName;
		URI uri = new URI(api);
		ResponseEntity<User> responseEntity = restTemplate.getForEntity(uri, User.class);
		User objects = responseEntity.getBody();
		return objects;
	}

	@Override
	public Optional<List<Transactions>> getTransactionByUserNameAndSessionId(String userName, String sessionId) {
		return transRepo.findByUserNameAndSessionId(userName, sessionId);
	}

	@Override
	public Optional<List<Transactions>> getPendingTransaction(String userName, SessionStatusEnum sessionStatusEnum) {
		return transRepo.findByUserNameAndSessionStatusEnum(userName, sessionStatusEnum);
	}

	@Override
	public Optional<List<Transactions>> getCompletedTransactions(String userName, SessionStatusEnum sessionStatusEnum) {
		return transRepo.findByUserNameAndSessionStatusEnum(userName, sessionStatusEnum);
	}

	@Override
	public Optional<List<Transactions>> getAllTransactionsByUserName(String userName) {
		return transRepo.findByUserName(userName);
	}

}
