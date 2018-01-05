package com.springboot.service;

import com.springboot.model.SysRoles;
import com.springboot.model.SysRolesPermissionsKey;

/**
 * 角色操作服务
 *
 * @author wangxh
 * @create 2018 01 05
 */
public interface RoleService {
    int addRole(SysRoles sysRoles);

    /**添加角色-权限之间关系*/
    void correlationPermissions(SysRolesPermissionsKey rolesPermissionsKey);

}
