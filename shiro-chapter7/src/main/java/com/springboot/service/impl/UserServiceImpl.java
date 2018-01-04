package com.springboot.service.impl;

import com.springboot.mapper.UsersMapper;
import com.springboot.model.Users;
import com.springboot.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class  UserServiceImpl implements UserService {

    @Resource
    private UsersMapper usersMapper;


    @Override
    public Users findUserById(Long id) {

        return usersMapper.selectByPrimaryKey(id);
    }

    @Override
    public int addUser(Users users) {
        return usersMapper.insert(users);
    }
}
