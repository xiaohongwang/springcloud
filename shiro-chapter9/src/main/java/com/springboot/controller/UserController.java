package com.springboot.controller;

import com.springboot.model.SysUsers;
import com.springboot.model.SysUsersRolesKey;
import com.springboot.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户信息的控制类
 *
 * @author wangxh
 * @create 2018 01 04
 */
@RestController
public class UserController {

    private static final Logger log =
            LoggerFactory.getLogger(UserController.class);
    @Resource
    private UserService userService;

    @RequestMapping("/addUser/{userName}/{password}")
    // @RequiresPermissions("addSysUser")  权限添加系统用户
    // @RequiresPermissions("sysUser:addSysUser")
    @RequiresRoles("sysUser")
    public String addUser(@PathVariable("userName") String userName, @PathVariable("password") String password){
        SysUsers users = new SysUsers();
        users.setUsername(userName);
        users.setPassword(password);
        userService.addUser(users);
        return "添加用户信息成功";
    }

    @Resource
    private EhCacheManager shiroEhcacheManager;

    @RequestMapping("/unFreezeUser/{username}")
    @RequiresPermissions("sysUser:unFreezeUser")
    public String unFreezeUser(@PathVariable("username") String username){
        shiroEhcacheManager.getCache("loginRetryCache").remove(username);
        SysUsers sysUsers =
                    userService.findByUsername(username);
        sysUsers.setLocked(false);
        userService.updateUserStatus(sysUsers);
        return "解冻用户给成功";
     }

    @RequestMapping("/changePassword/{username}/{oldPassword}/{newPassword}")
    @RequiresPermissions("sysUser:changePassword")
    public String changePassword(@PathVariable("username") String username,
                                 @PathVariable("oldPassword") String oldPassword,
                                 @PathVariable("newPassword") String newPassword){

        //先进行身份认证
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token =
                    new UsernamePasswordToken(username, oldPassword);

        try{

            subject.login(token);
            userService.changePassword(username, newPassword);
            return "用户更改密码成功";
        } catch (AuthenticationException e) {
            log.error("验证用户身份失败：原密码错误");
            return "用户更改密码失败";
        }

    }


    @RequestMapping("/correlationRoles/{roleId}/{userId}")
    @RequiresRoles("role")
    public String correlationRoles(
            @PathVariable("roleId") Long roleId,
            @PathVariable("userId") Long userId
    ){
        SysUsersRolesKey usersRolesKey =
                    new SysUsersRolesKey();
        usersRolesKey.setRoleId(roleId);
        usersRolesKey.setUserId(userId);
        userService.correlationRoles(usersRolesKey);
        return  "添加用户角色信息成功";
    }

    @RequestMapping("/findRoles/{username}")
    public String findRoles(@PathVariable("username") String username){
        List<String> roles = userService.findRoles(username);
        return roles.toString();
    }

    @RequestMapping("/findPermissions/{username}")
    public String findPermissions(@PathVariable("username") String username){
        List<String> roles = userService.findPermissions(username);
        return roles.toString();
    }
}
