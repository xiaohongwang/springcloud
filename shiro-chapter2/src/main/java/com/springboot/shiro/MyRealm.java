package com.springboot.shiro;

import com.springboot.model.Users;
import com.springboot.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

public class MyRealm extends AuthorizingRealm implements Comparable{

    private static  final Logger log = LoggerFactory.getLogger(MyRealm1.class);

    @Resource
    private UserService userService;

    @Override
    public String getName() {
        return "MyRealm";
    }

    @Override
    //仅支持UsernamePasswordToken类型的Token  判断Realm是否支持此Token
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof UsernamePasswordToken;
    }


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("调用名称为" + getName() + "的realm----------开始");
        //获取token中的用户名(身份)
        String userName = (String)authenticationToken.getPrincipal();

        //根据身份获取证明(从数据库中)
        Users users = userService.findUserById(1L);
        log.info("调用名称为" + getName() + "的realm----------结束");
        return new SimpleAuthenticationInfo(users, users.getPassword(), this.getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
