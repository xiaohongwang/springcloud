package com.springboot.service;


import com.springboot.model.SysUsers;
import com.springboot.model.SysUsersRolesKey;

import java.util.List;

public interface UserService {

    int addUser(SysUsers users);

    /**修改密码*/
    void changePassword(String userName, String newPassword);

    /**添加用户-角色关系*/
    void correlationRoles(SysUsersRolesKey sysUsersRolesKey);


    /**根据用户名查找用户*/
    SysUsers findByUsername(String username);

    /**根据用户名查找其角色*/
    List<String> findRoles(String username);

    /**根据用户名查找其权限*/
    List<String> findPermissions(String username);

    /**修改用户状态*/
    int updateUserStatus(SysUsers sysUsers);
}
