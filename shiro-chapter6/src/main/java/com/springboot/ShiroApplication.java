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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@SpringBootApplication
@Controller
@MapperScan(basePackages = "com.springboot.mapper")
public class ShiroApplication {

    private static final Logger log = LoggerFactory.getLogger(SimpleCredentialsMatcher.class);

    public static void main(String[] args){
        SpringApplication.run(ShiroApplication.class, args);
    }

    @RequestMapping("/hi")
    public String hi(){
        return "main";
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

          //需要将用户信息存入到session中
          log.info("用户身份认证通过" + subject.getPrincipals().asList().size());

          //单个资源单个权限  adm:user:add
          log.info("单个资源单个权限,结果======="+ subject.isPermitted("adm:user:add"));

          //单个资源多个权限 adm:user:update,delete
          log.info("单个资源多个权限，结果======="+ subject.isPermitted("adm:user:update,delete"));

          //单个资源全部权限 personal:user
          log.info("单个资源全部权限,结果======="+ subject.isPermitted("personal:user:*"));

          //所有资源全部权限
          //*:view
          log.info("单个资源全部权限,结果=======" + subject.isPermitted("XX:view"));
          //*:*:find
          log.info("单个资源全部权限,结果=======" + subject.isPermitted("XX:XX:find"));




      } catch (AuthenticationException e) {
          //身份认证失败
          log.error("用户身份认证失败");
      }
        return  "forward:/main";
    }

    @RequestMapping("/loginout")
    public String loginout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "main";
    }

    @RequestMapping("/main")
    public String main(){
        log.info("进行处理，调转到主页面");
        return "main";
    }
}
