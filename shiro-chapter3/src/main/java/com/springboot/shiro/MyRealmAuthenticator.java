package com.springboot.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.ArrayList;
import java.util.Collection;

public class MyRealmAuthenticator extends ModularRealmAuthenticator {

    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
        //判断getRealm() 是否返回为空
        assertRealmsConfigured();
        //强转token
        MyUserNamePasswordToken token =
                (MyUserNamePasswordToken) authenticationToken;

        Collection<Realm> realms = this.getRealms();

        String type = token.getType();

        // 登录类型为type对应的所有Realm
        Collection<Realm> typeRealms = new ArrayList<>();
        for (Realm realm : realms) {
            if (realm.getName().contains(type)){ // 比对规则，可根据业务更换
                    typeRealms.add(realm);
            }
        }

        //判断是单realm  还是多realm
        return typeRealms.size() == 1 ? this.doSingleRealmAuthentication((Realm)typeRealms.iterator().next(), authenticationToken)
                :  this.doMultiRealmAuthentication(typeRealms, authenticationToken);
    }
}
