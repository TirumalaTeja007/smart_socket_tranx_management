package com.sana.pomegranate.service;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.sana.pomegranate.enums.SessionStatusEnum;
import com.sana.pomegranate.model.Transactions;
import com.sana.pomegranate.model.User;

public interface ITransactionService {

	Optional<Transactions> addTransaction(@Valid Transactions transactions);

	Optional<Transactions> updateTransaction(@Valid Transactions transactions);

	Optional<List<Transactions>> getAllTransactions();

	User getUser(String userName) throws URISyntaxException;

	Optional<List<Transactions>> getTransactionByUserNameAndSessionId(String userName, String sessionId);

	Optional<List<Transactions>> getPendingTransaction(String userName,SessionStatusEnum sessionStatusEnum);

	Optional<List<Transactions>> getCompletedTransactions(String userName,SessionStatusEnum sessionStatusEnum);

	Optional<List<Transactions>> getAllTransactionsByUserName(String userName);
   
}
  