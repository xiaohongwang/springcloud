package com.springboot.service.impl;

import com.springboot.mapper.SysUsersMapper;
import com.springboot.mapper.SysUsersRolesMapper;
import com.springboot.model.SysUsers;
import com.springboot.model.SysUsersRolesKey;
import com.springboot.service.UserService;
import com.springboot.tool.MySimpleHas;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class  UserServiceImpl implements UserService {

    @Resource
    private SysUsersMapper sysUsersMapper;

    @Resource
    private SysUsersRolesMapper sysUsersRolesMapper;

    @Override
    public int addUser(SysUsers sysUsers) {
        String userName = sysUsers.getUsername();
        String password = sysUsers.getPassword();

        Map<String, String> result =
                MySimpleHas.getEncryPass(userName, password);

        sysUsers.setPassword(result.get("encryPassword"));
        sysUsers.setSalt(result.get("salt"));

        return sysUsersMapper.insert(sysUsers);
    }

    @Override
    public void changePassword(String userName, String newPassword) {
        SysUsers sysUsers = sysUsersMapper.findByUsername(userName);
        Map<String, String> result =
                MySimpleHas.getEncryPass(sysUsers.getUsername(), newPassword);

        sysUsers.setPassword(result.get("encryPassword"));
        sysUsers.setSalt(result.get("salt"));

        sysUsersMapper.updateByPrimaryKeySelective(sysUsers);
    }

    @Override
    public void correlationRoles(SysUsersRolesKey sysUsersRolesKey) {
        sysUsersRolesMapper.insert(sysUsersRolesKey);
    }


    @Override
    public SysUsers findByUsername(String username) {
        return sysUsersMapper.findByUsername(username);
    }

    @Override
    public List<String> findRoles(String username) {
        return sysUsersMapper.findRoles(username);
    }

    @Override
    public List<String> findPermissions(String username) {
        return sysUsersMapper.findPermissions(username);
    }

    @Override
    public int updateUserStatus(SysUsers sysUsers) {
        return sysUsersMapper.updateByPrimaryKeySelective(sysUsers);
    }
}
