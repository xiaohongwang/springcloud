package com.springboot.shiro;


import com.springboot.model.SysUsers;
import com.springboot.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

public class MyRealm extends AuthorizingRealm {

    private static  final Logger log = LoggerFactory.getLogger(MyRealm.class);

    @Resource
    private UserService userService;

    @Override
    public String getName() {
        return "MyRealm";
    }

    @Override
    /**仅支持UsernamePasswordToken类型的Token  判断Realm是否支持此Token*/
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof UsernamePasswordToken;
    }


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("调用名称为" + getName() + "的realm----------开始");
        //获取token中的用户名(身份)
        String userName = (String)authenticationToken.getPrincipal();

        //根据身份获取证明(从数据库中)
        SysUsers users = userService.findByUsername(userName);
        log.info("调用名称为" + getName() + "的realm----------结束");
        SimpleAuthenticationInfo info
                = new SimpleAuthenticationInfo(users, users.getPassword(), this.getName());
        info.setCredentialsSalt(ByteSource.Util.bytes(users.getUsername() + users.getSalt()));
        return info;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //Shiro不负责维护用户-角色信息，需要应用提供
        Subject subject = SecurityUtils.getSubject();
        SysUsers user = (SysUsers)subject.getPrincipal();

        String userName = user.getUsername();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //权限
        info.addStringPermissions(userService.findPermissions(userName));
        //角色
        info.addRoles(userService.findRoles(userName));
        return info;
    }

}
