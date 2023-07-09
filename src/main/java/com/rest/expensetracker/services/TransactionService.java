package com.rest.expensetracker.services;

import com.rest.expensetracker.domain.Transaction;
import com.rest.expensetracker.exceptions.EtBadRequestException;
import com.rest.expensetracker.exceptions.EtResourceNotFoundException;

import java.util.*;
public interface TransactionService {

    List<Transaction> fetchAllTransaction(Integer userId, Integer catagoryId);

    Transaction fetchTransactionById(Integer userId,Integer catagoryId,Integer transactionId) throws EtResourceNotFoundException;

    Transaction addTransaction(Integer userId,Integer catagoryId, Double amount,String note, Long transactionDate) throws EtBadRequestException;

    void updateTransaction(Integer userId,Integer catagoryId,Integer transactionId,Transaction transaction) throws EtBadRequestException;

    void removeTransaction(Integer userId,Integer catagoryId,Integer transactionId) throws EtResourceNotFoundException;

}
