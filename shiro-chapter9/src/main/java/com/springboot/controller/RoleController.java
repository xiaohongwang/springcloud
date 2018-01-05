package com.springboot.controller;

import com.springboot.model.SysRoles;
import com.springboot.model.SysRolesPermissionsKey;
import com.springboot.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 角色操作入口
 *
 * @author wangxh
 * @create 2018 01 05
 */
@RestController
public class RoleController {

    @Resource
    private RoleService roleService;

    @RequestMapping("/addRole/{role}/{desc}")
    @RequiresRoles("role")
    public String addRole(
            @PathVariable("role") String role,
            @PathVariable("desc") String desc
    ){
        SysRoles roles = new SysRoles();
        roles.setRole(role);
        roles.setDescription(desc);
        roleService.addRole(roles);
        return "添加角色成功";
    }

    @RequestMapping("/correlationPermissions/{roleId}/{permissionId}")
    @RequiresRoles("role")
    public String correlationPermissions(
            @PathVariable("roleId") Long roleId,
            @PathVariable("permissionId") Long permissionId)
    {
        SysRolesPermissionsKey rolesPermissionsKey =
                    new SysRolesPermissionsKey();
        rolesPermissionsKey.setPermissionId(permissionId);
        rolesPermissionsKey.setRoleId(roleId);
        roleService.correlationPermissions(rolesPermissionsKey);
        return "添加角色权限关系成功";
    }


}
