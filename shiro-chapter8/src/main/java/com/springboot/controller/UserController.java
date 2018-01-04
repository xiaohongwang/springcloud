package com.springboot.controller;

import com.springboot.model.Users;
import com.springboot.service.UserService;
import com.springboot.tool.MySimpleHas;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 用户信息的控制类
 *
 * @author wangxh
 * @create 2018 01 04
 */
@RestController
public class UserController {
    @Resource
    private UserService userService;

    @RequestMapping("/addUser/{userName}/{password}")
    public String addUser(@PathVariable("userName") String userName, @PathVariable("password") String password){
        Users users = new Users();
        users.setUsername(userName);
        Map<String, String> result =
                MySimpleHas.getEncryPass(userName, password);

        users.setPassword(result.get("encryPassword"));
        users.setPasswordSalt(result.get("salt"));

        userService.addUser(users);
        return "添加用户信息成功";
    }

    @Value("${name}")
    private String username;

    @Resource
    private EhCacheManager shiroEhcacheManager;

    @RequestMapping("/unFreezeUser")
    public String unFreezeUser(){
        shiroEhcacheManager.getCache("loginRetryCache").remove(username);
        return "解冻用户给成功";
 }
}
