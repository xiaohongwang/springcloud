package com.springboot.controller;

import com.springboot.model.SysPermissions;
import com.springboot.service.PermissionService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 权限操作入口
 *
 * @author wangxh
 * @create 2018 01 05
 */
@RestController
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    @RequestMapping("/addPermission/{permisson}/{desc}")
    @RequiresRoles("permisson")
    public String addPermisson(
            @PathVariable("permisson") String permisson,
            @PathVariable("desc") String desc
    ){
        SysPermissions permissions = new SysPermissions();
        permissions.setPermission(permisson);
        permissions.setDescription(desc);
        permissionService.addPermissions(permissions);
        return "添加权限信息成功";
    }
}
