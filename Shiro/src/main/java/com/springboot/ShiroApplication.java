package com.springboot;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.subject.Subject;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
@MapperScan(basePackages = "com.springboot.mapper")
public class ShiroApplication {

    private static final Logger log = LoggerFactory.getLogger(SimpleCredentialsMatcher.class);

    public static void main(String[] args){
        SpringApplication.run(ShiroApplication.class, args);
    }

    @RequestMapping("/hi")
    public String hi(){
        return "Hello Shiro!";
    }

    @Value("${name}")
    private String username;

    @Value("${password}")
    private String password;

    @RequestMapping("/login")
    public String login(){
        //创建Token
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        // 获取用户subject
        Subject subject = SecurityUtils.getSubject();

      try{
          //身份认证
          subject.login(token);
          log.info("用户身份认证通过");
      } catch (AuthenticationException e) {
          //身份认证失败
          log.error("用户身份认证失败");
      }

        return "true";
    }

}
