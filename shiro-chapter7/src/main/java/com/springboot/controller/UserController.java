package com.springboot.controller;

import com.springboot.model.Users;
import com.springboot.service.UserService;
import com.springboot.tool.MySimpleHas;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;
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
}
