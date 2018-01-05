package com.springboot.service.impl;

import com.springboot.mapper.SysPermissionsMapper;
import com.springboot.model.SysPermissions;
import com.springboot.service.PermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wangxh
 * @create 2018 01 05
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private SysPermissionsMapper sysPermissionsMapper;
    @Override
    public int addPermissions(SysPermissions sysPermissions) {
        return sysPermissionsMapper.insert(sysPermissions);
    }

}
