package com.springboot.shiro;

import com.springboot.model.Users;
import com.springboot.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
public class MyRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;

    @Override
    //仅支持UsernamePasswordToken类型的Token  判断Realm是否支持此Token
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof UsernamePasswordToken;
    }


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取token中的用户名(身份)
        String userName = (String)authenticationToken.getPrincipal();

        //根据身份获取证明(从数据库中)
        Users users = userService.findUserById(1L);
        return new SimpleAuthenticationInfo(users, users.getPassword(), this.getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }
}
