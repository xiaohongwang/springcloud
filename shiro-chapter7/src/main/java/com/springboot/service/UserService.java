package com.springboot.service;


import com.springboot.model.Users;
import org.apache.catalina.User;

public interface UserService {
    Users findUserById(Long id);

    int addUser(Users users);
}
