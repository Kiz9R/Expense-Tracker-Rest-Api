package com.rest.expensetracker.repositories;

import com.rest.expensetracker.domain.Transaction;
import com.rest.expensetracker.exceptions.EtBadRequestException;
import com.rest.expensetracker.exceptions.EtResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository{

    private static final String SQL_CREATE = "INSERT INTO ET_TRANSACTIONS (TRANSACTION_ID, CATAGORY_ID, USER_ID, AMOUNT, NOTE, TRANSACTION_DATE) VALUES(NEXTVAL('ET_TRANSACTIONS_SEQ'), ?, ?, ?, ?, ?)";
    private static final String SQL_FIND_BY_ID = "SELECT TRANSACTION_ID, CATAGORY_ID, USER_ID, AMOUNT, NOTE, TRANSACTION_DATE FROM ET_TRANSACTIONS WHERE USER_ID = ? AND CATAGORY_ID = ? AND TRANSACTION_ID = ?";
    private static final String SQL_FIND_ALL = "SELECT TRANSACTION_ID, CATAGORY_ID, USER_ID, AMOUNT, NOTE, TRANSACTION_DATE FROM ET_TRANSACTIONS WHERE USER_ID = ? AND CATAGORY_ID = ?";
    private static final String SQL_UPDATE = "UPDATE ET_TRANSACTIONS SET AMOUNT = ?, NOTE = ?, TRANSACTION_DATE = ? WHERE USER_ID = ? AND CATAGORY_ID = ? AND TRANSACTION_ID = ?";
    private static final String SQL_DELETE = "DELETE FROM ET_TRANSACTIONS WHERE USER_ID = ? AND CATAGORY_ID = ? AND TRANSACTION_ID = ?";
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Transaction> findAll(Integer userId, Integer catagoryId) {
        return jdbcTemplate.query(SQL_FIND_ALL,new Object[]{userId,catagoryId},transactionRowMapper);
    }

    @Override
    public Transaction findById(Integer userId, Integer catagoryId, Integer transactionId) throws EtResourceNotFoundException {
        try{
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{userId,catagoryId,transactionId}, transactionRowMapper);
        }catch(Exception e){
            throw new EtResourceNotFoundException("Transaction Not Found");
        }
    }

    @Override
    public Integer create(Integer userId, Integer catagoryId, Double amount, String note, Long transactionDate) throws EtBadRequestException {
        try{
            KeyHolder keyHolder=new GeneratedKeyHolder();
            jdbcTemplate.update(connection ->{
                PreparedStatement ps=connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1,catagoryId);
                ps.setInt(2,userId);
                ps.setDouble(3,amount);
                ps.setString(4,note);
                ps.setLong(5,transactionDate);
                return ps;
            },keyHolder);
            return (Integer) keyHolder.getKeys().get("TRANSACTION_ID");
        }catch(Exception e){
            throw new EtBadRequestException("Invalid Create Request");
        }
    }

    @Override
    public void update(Integer userId, Integer catagoryId, Integer transactionId, Transaction transaction) throws EtBadRequestException {
        try{
            jdbcTemplate.update(SQL_UPDATE,new Object[]{transaction.getAmount(),transaction.getNote(),transaction.getTransactionDate(),userId,catagoryId,transactionId});
        }catch(Exception e){
            throw new EtBadRequestException("Invalid Update Request");
        }
    }

    @Override
    public void removeById(Integer userId, Integer catagoryId, Integer transactionId) throws EtResourceNotFoundException {
        int count=jdbcTemplate.update(SQL_DELETE,new Object[]{userId,catagoryId,transactionId});
        if(count==0)
            throw new EtResourceNotFoundException("Transaction Not Found");
    }

    private RowMapper<Transaction> transactionRowMapper=(rs,rowNum)->{
        return new Transaction(rs.getInt("TRANSACTION_ID"),
                rs.getInt("CATAGORY_ID"),
                rs.getInt("USER_ID"),
                rs.getDouble("AMOUNT"),
                rs.getString("NOTE"),
                rs.getLong("TRANSACTION_DATE"));
    };
}
