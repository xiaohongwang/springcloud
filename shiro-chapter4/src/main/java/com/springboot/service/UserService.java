package com.springboot.service;


import com.springboot.model.Users;

public interface UserService {
    Users findUserById(Long id);
}
