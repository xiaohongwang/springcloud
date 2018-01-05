package com.springboot.service;

import com.springboot.model.SysPermissions;

/**
 * 权限操作服务
 *
 * @author wangxh
 * @create 2018 01 04
 */
public interface PermissionService {
    int addPermissions(SysPermissions sysPermissions);
}
