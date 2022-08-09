package com.sana.pomegranate.controller;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sana.pomegranate.model.User;
import com.sana.pomegranate.dto.ApiResponse;
import com.sana.pomegranate.enums.SessionStatusEnum;
import com.sana.pomegranate.exception.ResourceRegistrationException;
import com.sana.pomegranate.exception.ResourceUpdationException;
import com.sana.pomegranate.model.Transactions;
import com.sana.pomegranate.service.ITransactionService;

@Controller
@CrossOrigin(origins="*")
@RequestMapping("/api")
public class TransactionController {
	
	private static Logger logger = LogManager.getLogger(TransactionController.class);
	
	@Autowired
	ITransactionService transactionService;
	
	@PostMapping(value = "/transaction")
	public ResponseEntity<ApiResponse> addTransaction(@RequestBody @Valid Transactions transactions, @RequestHeader(value="", required=true) String password) throws URISyntaxException {
		logger.info("TransactionController.addTransaction()::Transactions:" + transactions);
		
		User user = transactionService.getUser(transactions.getUserName());
		
		if(password.equals(user.getPassword()) == true) {
			return transactionService.addTransaction(transactions).map(newTransactions -> {
				ApiResponse apiResponse = null;
				logger.info("Transactions returned [API[: " + newTransactions);
				apiResponse=new ApiResponse("Transaction is added successfully.", true);
				apiResponse.setData(newTransactions);
				return ResponseEntity.ok(apiResponse);
			}).orElseThrow(() -> new ResourceRegistrationException("Transactions", "Transactions registration is failed"));
		} else {
			throw new ResourceRegistrationException("UserName & Password", "Unauthorized request is registered.");
		}
	}
	
	@PutMapping(value = "/transaction")
	public ResponseEntity<ApiResponse> updateTransaction(@RequestBody @Valid Transactions transactions, @RequestHeader(value="", required=true) String password) throws URISyntaxException {
		
		logger.info("TransactionController.updateTransaction()::Transactions:" + transactions);
		
		User user = transactionService.getUser(transactions.getUserName());
		
		if(password.equals(user.getPassword()) == true) {
			return transactionService.updateTransaction(transactions).map(newTransactions -> {
				ApiResponse apiResponse = null;
				logger.info("Transactions returned [API[: " + newTransactions);
				apiResponse=new ApiResponse("Transaction is updated successfully.", true);
				apiResponse.setData(newTransactions);
				return ResponseEntity.ok(apiResponse);
			}).orElseThrow(() -> new ResourceUpdationException("Transactions", "Transactions Updation is failed"));
		}else {
			throw new ResourceRegistrationException("UserName & Password", "Unauthorized request is registered.");
		}
	}	
	
	@GetMapping(value = "/transaction/{userName}")
	public ResponseEntity<?> getAllTransactions(@PathVariable @Valid String userName, @RequestHeader(value="", required=true) String password) throws URISyntaxException {
		
		logger.info("TransactionController.getAllTransactions() details ::");
		
		User user = transactionService.getUser(userName);
		
		
		if(password.equals(user.getPassword()) == true) {
			Optional<List<Transactions>> transactionsList = transactionService.getAllTransactionsByUserName(userName);
			ApiResponse apiResponse=null;
			if(transactionsList.isPresent()) {
				apiResponse=new ApiResponse("Transactions are Not Found.", false);
			}else {
				apiResponse=new ApiResponse("Transactions are found successfully.", true);
			}
			apiResponse.setData(transactionsList.get());
			return ResponseEntity.ok(apiResponse);
		}else {
			throw new ResourceRegistrationException("UserName & Password", "Unauthorized request is registered.");
		}
		
	}
	 
	
	@GetMapping(value = "/transaction/pending/{userName}")
	public ResponseEntity<?> getPendingTransactions(@PathVariable @Valid String userName, @RequestHeader(value="", required=true) String password) throws URISyntaxException {
		
		logger.info("TransactionController.getAllTransactions() details ::");
		
		User user = transactionService.getUser(userName);
		
		
		if(password.equals(user.getPassword()) == true) {
			SessionStatusEnum sessionStatusEnum = SessionStatusEnum.START;
			Optional<List<Transactions>> transactionsList = transactionService.getPendingTransaction(userName, sessionStatusEnum);
			ApiResponse apiResponse=null;
			if(transactionsList.isPresent()) {
				apiResponse=new ApiResponse("Transactions are Not Found.", false);
			}else {
				apiResponse=new ApiResponse("Transactions are found successfully.", true);
			}
			apiResponse.setData(transactionsList.get());
			return ResponseEntity.ok(apiResponse);
		}else {
			throw new ResourceRegistrationException("UserName & Password", "Unauthorized request is registered.");
		}
		
	}
	
	@GetMapping(value = "/transaction/completed/{userName}")
	public ResponseEntity<?> getCompletedTransactions(@PathVariable @Valid String userName, @RequestHeader(value="", required=true) String password) throws URISyntaxException {
		
		logger.info("TransactionController.getAllTransactions() details ::");
		
		User user = transactionService.getUser(userName);
		
		
		if(password.equals(user.getPassword()) == true) {
			SessionStatusEnum sessionStatusEnum = SessionStatusEnum.STOP;
			Optional<List<Transactions>> transactionsList = transactionService.getCompletedTransactions(userName, sessionStatusEnum);
			ApiResponse apiResponse=null;
			if(transactionsList.isPresent()) {
				apiResponse=new ApiResponse("Transactions are Not Found.", false);
			}else {
				apiResponse=new ApiResponse("Transactions are found successfully.", true);
			}
			apiResponse.setData(transactionsList.get());
			return ResponseEntity.ok(apiResponse);
		}else {
			throw new ResourceRegistrationException("UserName & Password", "Unauthorized request is registered.");
		}
		
	}
	
	@GetMapping(value = "/transaction/sessionId/{userName}/{sessionId}")
	public ResponseEntity<?> getAllTransactions(@PathVariable @Valid String userName, @PathVariable @Valid String sessionId, @RequestHeader(value="", required=true) String password) throws URISyntaxException {
		
		logger.info("TransactionController.getAllTransactions() details ::");
		
		User user = transactionService.getUser(userName);
		
		if(password.equals(user.getPassword()) == true) {
			Optional<List<Transactions>> transactionsList = transactionService.getTransactionByUserNameAndSessionId(userName, sessionId);
			ApiResponse apiResponse=null;
			if(transactionsList.isPresent()) {
				apiResponse=new ApiResponse("Transactions are Not Found.", false);
			}else {
				apiResponse=new ApiResponse("Transactions are found successfully.", true);
			}
			apiResponse.setData(transactionsList.get());
			return ResponseEntity.ok(apiResponse);
		}else {
			throw new ResourceRegistrationException("UserName & Password", "Unauthorized request is registered.");
		}
		
	}

}
