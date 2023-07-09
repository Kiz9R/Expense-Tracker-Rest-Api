package com.rest.expensetracker.services;

import com.rest.expensetracker.domain.User;
import com.rest.expensetracker.exceptions.EtAuthException;

public interface UserService {
    User validateUser(String email, String password) throws EtAuthException;

    User registerUser(String firstName, String lastName, String email, String password) throws EtAuthException;
}
