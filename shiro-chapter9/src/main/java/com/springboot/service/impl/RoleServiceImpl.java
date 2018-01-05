package com.springboot.service.impl;

import com.springboot.mapper.SysRolesMapper;
import com.springboot.mapper.SysRolesPermissionsMapper;
import com.springboot.model.SysRoles;
import com.springboot.model.SysRolesPermissionsKey;
import com.springboot.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wangxh
 * @create 2018 01 05
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Resource
    private SysRolesMapper sysRolesMapper;

    @Resource
    private SysRolesPermissionsMapper sysRolesPermissionsMapper;

    @Override
    public int addRole(SysRoles sysRoles) {
        return sysRolesMapper.insert(sysRoles);
    }

    @Override
    public void correlationPermissions(SysRolesPermissionsKey rolesPermissionsKey) {
        sysRolesPermissionsMapper.insert(rolesPermissionsKey);
    }

}
