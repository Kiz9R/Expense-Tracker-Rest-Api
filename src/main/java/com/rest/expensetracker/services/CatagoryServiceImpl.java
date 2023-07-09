package com.rest.expensetracker.services;

import com.rest.expensetracker.domain.Catagory;
import com.rest.expensetracker.exceptions.EtBadRequestException;
import com.rest.expensetracker.exceptions.EtResourceNotFoundException;
import com.rest.expensetracker.repositories.CatagoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CatagoryServiceImpl implements CatagoryService{

    @Autowired
    CatagoryRepository catagoryRepository;
    @Override
    public List<Catagory> fetchAllCatagories(Integer userId) {
        return catagoryRepository.findAll(userId);
    }

    @Override
    public Catagory fetchAllCatagoryById(Integer userId, Integer catagoryId) throws EtResourceNotFoundException {
        return catagoryRepository.findById(userId,catagoryId);
    }

    @Override
    public Catagory addCatagory(Integer userId, String title, String description) throws EtBadRequestException {
        int catagoryId=catagoryRepository.create(userId,title,description);
        return catagoryRepository.findById(userId,catagoryId);
    }

    @Override
    public void updateCatagory(Integer userId, Integer catagoryId, Catagory catagory) throws EtBadRequestException {
        catagoryRepository.update(userId, catagoryId, catagory);
    }

    @Override
    public void removeCatagoryWithAllTransactions(Integer userId, Integer catagoryId) throws EtResourceNotFoundException {
        this.fetchAllCatagoryById(userId, catagoryId);
        catagoryRepository.removeById(userId, catagoryId);
    }
}
