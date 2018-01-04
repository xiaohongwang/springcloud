package com.springboot.tool;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangxh
 */
public class MySimpleHas {
    public static Map<String, String> getEncryPass(String userName, String password){
        String algorithmName = "md5";
        String salt1 = userName;
        String salt2 = new SecureRandomNumberGenerator().nextBytes().toHex();
        int hashIterations = 2;

        SimpleHash hash = new SimpleHash(algorithmName, password, salt1 + salt2, hashIterations);
        String encodedPassword = hash.toHex();

        Map<String, String> result =
                new HashMap<>();
        result.put("salt", salt2);
        result.put("encryPassword", encodedPassword);
        return result;
    }

    public static String getEncryPassword(String salt, String password){
        String algorithmName = "md5";
        int hashIterations = 2;

        SimpleHash hash = new SimpleHash(algorithmName, password, salt, hashIterations);
        String encodedPassword = hash.toHex();

        return encodedPassword;
    }
}
