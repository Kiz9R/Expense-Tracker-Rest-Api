package com.rest.expensetracker.services;

import com.rest.expensetracker.domain.Catagory;
import com.rest.expensetracker.exceptions.EtBadRequestException;
import com.rest.expensetracker.exceptions.EtResourceNotFoundException;

import java.util.*;

public interface CatagoryService {

    List<Catagory> fetchAllCatagories(Integer userId);

    Catagory fetchAllCatagoryById(Integer userId,Integer catagoryId) throws EtResourceNotFoundException;

    Catagory addCatagory(Integer userId, String title,String description) throws EtBadRequestException;

    void updateCatagory(Integer userId,Integer catagoryId,Catagory catagory) throws EtBadRequestException;

    void removeCatagoryWithAllTransactions(Integer userId,Integer catagoryId) throws EtResourceNotFoundException;
}
