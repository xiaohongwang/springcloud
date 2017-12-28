package com.springboot.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import com.springboot.tool.MD5;


public class MyCredential extends SimpleCredentialsMatcher{

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //可在进行认证前进行相关信息处理
        UsernamePasswordToken userToken = (UsernamePasswordToken) token;
        String username = userToken.getUsername();
        String password = String.valueOf(userToken.getPassword());

        String encryptPassword = MD5.getMd5Hash(username, password);
        userToken.setPassword(encryptPassword.toCharArray());
        return super.doCredentialsMatch(userToken, info);
    }
}
