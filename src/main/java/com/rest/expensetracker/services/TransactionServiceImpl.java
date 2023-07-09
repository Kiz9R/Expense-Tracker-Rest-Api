package com.rest.expensetracker.services;

import com.rest.expensetracker.domain.Transaction;
import com.rest.expensetracker.exceptions.EtBadRequestException;
import com.rest.expensetracker.exceptions.EtResourceNotFoundException;
import com.rest.expensetracker.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    TransactionRepository transactionRepository;
    @Override
    public List<Transaction> fetchAllTransaction(Integer userId, Integer catagoryId) {
        return transactionRepository.findAll(userId, catagoryId);
    }

    @Override
    public Transaction fetchTransactionById(Integer userId, Integer catagoryId, Integer transactionId) throws EtResourceNotFoundException {
        return transactionRepository.findById(userId, catagoryId, transactionId);
    }

    @Override
    public Transaction addTransaction(Integer userId, Integer catagoryId, Double amount, String note, Long transactionDate) throws EtBadRequestException {
        int transactionId=transactionRepository.create(userId, catagoryId, amount, note, transactionDate);
        return transactionRepository.findById(userId,catagoryId,transactionId);
    }

    @Override
    public void updateTransaction(Integer userId, Integer catagoryId, Integer transactionId, Transaction transaction) throws EtBadRequestException {
        transactionRepository.update(userId, catagoryId, transactionId, transaction);
    }

    @Override
    public void removeTransaction(Integer userId, Integer catagoryId, Integer transactionId) throws EtResourceNotFoundException {
        transactionRepository.removeById(userId, catagoryId, transactionId);
    }
}
