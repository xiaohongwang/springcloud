package com.springboot.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 密码重试次数限制
 * 在1个小时内密码最多重试5次，
 * 如果尝试次数超过5次就锁定1小时，1小时后可再次重试 ,
 * 如果还是重试失败，可以锁定如1天，以此类推，防止密码被暴力破解
 * @author wangxh
 * @create 2018 01 04
 */
public class RetryLimitHashedCredential extends HashedCredentialsMatcher {

    private static final Logger log =
            LoggerFactory.getLogger(RetryLimitHashedCredential.class);
    //集群中可能会导致出现验证多过5次的现象，因为AtomicInteger只能保证单节点并发
    private Cache<String, AtomicInteger> loginRetryCache;

    public RetryLimitHashedCredential(EhCacheManager ehCacheManager) {
            loginRetryCache = ehCacheManager.getCache("loginRetryCache");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
            String username = (String)token.getPrincipal();
            AtomicInteger retryCount = loginRetryCache.get(username);

            if (null == retryCount) {
                retryCount = new AtomicInteger(0);
                loginRetryCache.put(username, retryCount);
            }
            if(retryCount.incrementAndGet() > 2) {
                    //if retry count > 2 throw
                log.info("username: " + username + " tried to login more than 5 times in period");
                throw new ExcessiveAttemptsException("username: " + username + " tried to login more than 5 times in period");
            }

            boolean matches = super.doCredentialsMatch(token, info);
            if(matches) {
                //clear retry count
                loginRetryCache.remove(username);
            }
            return matches;
    }
}
