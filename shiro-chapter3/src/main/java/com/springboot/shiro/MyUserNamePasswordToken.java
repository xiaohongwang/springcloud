package com.springboot.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class MyUserNamePasswordToken extends UsernamePasswordToken{
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MyUserNamePasswordToken(String userName, String password, String type){
        super(userName, password);
        this.type = type;
    }
}
