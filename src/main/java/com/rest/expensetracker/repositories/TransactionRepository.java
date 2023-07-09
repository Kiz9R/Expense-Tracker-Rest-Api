package com.rest.expensetracker.repositories;

import com.rest.expensetracker.domain.Transaction;
import com.rest.expensetracker.exceptions.EtBadRequestException;
import com.rest.expensetracker.exceptions.EtResourceNotFoundException;

import java.util.*;
public interface TransactionRepository {

    List<Transaction> findAll(Integer userId,Integer catagoryId);

    Transaction findById(Integer userId,Integer catahoryId,Integer TransactionId) throws EtResourceNotFoundException;

    Integer create(Integer userId,Integer catagoryId,Double amount,String note,Long transactionDate) throws EtBadRequestException;

    void update(Integer userId,Integer catagoryId,Integer transactionId,Transaction transaction) throws EtBadRequestException;

    void removeById(Integer userId,Integer catagoryId,Integer transactionId) throws EtResourceNotFoundException;
}
