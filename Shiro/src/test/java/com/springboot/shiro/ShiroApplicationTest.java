package com.springboot.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShiroApplicationTest {

    @Value("${username}")
    private String username;

    @Value("${password}")
    private String password;

    @Test
    public void login() {

        //创建Token 传入用户手动输入的用户名与密码
        UsernamePasswordToken token =
                new UsernamePasswordToken(username, password);

        //2.创建Subject对象   就是一个“用户”
        Subject subject = SecurityUtils.getSubject();

        //3.通过subject实现登陆,该方法会调用Shiro内部的校验，
            // 如果校验不通过则会抛出异常信息。

        try{
            //4、登录、身份认证
            subject.login(token);
        } catch (AuthenticationException e) {
            //5、身份认证失败
        }
        //断言用户已经登录
        Assert.assertEquals(true, subject.isAuthenticated());
    }
}
