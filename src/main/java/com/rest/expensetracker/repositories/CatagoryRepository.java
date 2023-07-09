package com.rest.expensetracker.repositories;

import java.util.*;

import com.rest.expensetracker.domain.Catagory;
import com.rest.expensetracker.exceptions.EtBadRequestException;
import com.rest.expensetracker.exceptions.EtResourceNotFoundException;

public interface CatagoryRepository {

    List<Catagory> findAll(Integer userId) throws EtResourceNotFoundException;

    Catagory findById(Integer userId ,Integer catagoryId) throws EtResourceNotFoundException;

    Integer create(Integer userId,String title, String description) throws EtBadRequestException;

    void update(Integer userId, Integer catagoryId, Catagory catagory) throws EtBadRequestException;

    void removeById(Integer userId,Integer catagoryId);
}
