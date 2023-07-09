package com.rest.expensetracker.repositories;

import com.rest.expensetracker.domain.Catagory;
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
public class CatagoryRepositoryImpl implements CatagoryRepository {

    private static final String SQL_FIND_ALL="SELECT C.CATAGORY_ID, C.USER_ID, C.TITLE, C.DESCRIPTION, " +
            "COALESCE(SUM(T.AMOUNT), 0) TOTAL_EXPENSE " +
            "FROM ET_TRANSACTIONS T RIGHT OUTER JOIN ET_CATAGORIES C ON C.CATAGORY_ID = T.CATAGORY_ID " +
            "WHERE C.USER_ID = ? GROUP BY C.CATAGORY_ID";
    private static final String SQL_FIND_BY_ID = "SELECT C.CATAGORY_ID, C.USER_ID, C.TITLE, C.DESCRIPTION, " +
            "COALESCE(SUM(T.AMOUNT), 0) TOTAL_EXPENSE " +
            "FROM ET_TRANSACTIONS T RIGHT OUTER JOIN ET_CATAGORIES C ON C.CATAGORY_ID = T.CATAGORY_ID " +
            "WHERE C.USER_ID = ? AND C.CATAGORY_ID = ? GROUP BY C.CATAGORY_ID";
    private static final String SQL_CREATE="INSERT INTO ET_CATAGORIES (CATAGORY_ID,USER_ID,TITLE,DESCRIPTION) VALUES(NEXTVAL('ET_CATAGORIES_SEQ'), ?, ?, ?)";

    private static final String SQL_UPDATE = "UPDATE ET_CATAGORIES SET TITLE = ?, DESCRIPTION = ? " +
            "WHERE USER_ID = ? AND CATAGORY_ID = ?";

    private static final String SQL_DELETE_CATAGORY = "DELETE FROM ET_CATAGORIES WHERE USER_ID = ? AND CATAGORY_ID = ?";

    private static final String SQL_DELETE_ALL_TRANSACTIONS = "DELETE FROM ET_TRANSACTIONS WHERE CATAGORY_ID = ?";


    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Catagory> findAll(Integer userId) throws EtResourceNotFoundException {
        return jdbcTemplate.query(SQL_FIND_ALL,new Object[]{userId},catagoryRowMapper);
    }

    @Override
    public Catagory findById(Integer userId, Integer catagoryId) throws EtResourceNotFoundException {
        try{
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID,new Object[]{userId,catagoryId},catagoryRowMapper);
        }catch (Exception e){
            throw new EtResourceNotFoundException("Category not found");
        }
    }

    @Override
    public Integer create(Integer userId, String title, String description) throws EtBadRequestException {
        try{
            KeyHolder keyHolder=new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, userId);
                ps.setString(2, title);
                ps.setString(3, description);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("CATAGORY_ID");
        }catch(Exception e){
            throw new EtBadRequestException("Bad Request");
        }
    }

    @Override
    public void update(Integer userId, Integer catagoryId, Catagory catagory) throws EtBadRequestException {
        try{
            jdbcTemplate.update(SQL_UPDATE, new Object[]{catagory.getTitle(),catagory.getDescription(), userId,catagoryId});
        }catch (Exception e){
            throw new EtBadRequestException("Bad Request");
        }
    }

    @Override
    public void removeById(Integer userId, Integer catagoryId) {
        this.removeAllTransactions(catagoryId);
        jdbcTemplate.update(SQL_DELETE_CATAGORY,new Object[]{userId,catagoryId});
    }

    private void removeAllTransactions(Integer catagoryId){
        jdbcTemplate.update(SQL_DELETE_ALL_TRANSACTIONS,new Object[]{catagoryId});
    }


    private RowMapper<Catagory> catagoryRowMapper=((rs,rowNum)->{
       return new Catagory(rs.getInt("CATAGORY_ID"),
               rs.getInt("USER_ID"),
               rs.getString("TITLE"),
               rs.getString("DESCRIPTION"),
               rs.getDouble("TOTAL_EXPENSE"));
    });
}
