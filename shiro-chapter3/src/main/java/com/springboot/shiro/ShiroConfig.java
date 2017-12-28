package com.springboot.shiro;

import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    //拦截器
    private static Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

    @Bean(name = "lifecycleBeanPostProcessor")
    //管理Shiro的生命周期，初始化和销毁
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean("shiroRealm")
    //配置权限登录器
    public Realm getShiroRealm(){
        AuthorizingRealm realm =  new MyRealm();
        //自定义比较器
        realm.setCredentialsMatcher(new MyCredential());
        return realm;
    }

    @Bean("shiroRealm1")
    //配置权限登录器
    public Realm getShiroRealm1(){
        AuthorizingRealm realm =  new MyRealm1();
        //自定义比较器   --  realm传入的认证信息是经过前一个realm处理后的信息
        // realm.setCredentialsMatcher(new MyCredential());
        return realm;
    }

    @Bean("shiroAdmRealm")
    //配置权限登录器
    public Realm getShiroAdmRealm(){
        AuthorizingRealm realm =  new AdminRealm();
        //自定义比较器   --  realm传入的认证信息是经过前一个realm处理后的信息
        realm.setCredentialsMatcher(new MyCredential());
        return realm;
    }

    @Bean(name = "shiroEhcacheManager")
    /*
        缓存管理，用户登陆成功后，把用户信息和权限信息缓存起来，然后每次用户请求时，
        放入用户的session中，如果不设置这个bean，每个请求都会查询一次数据库。
     */
    public EhCacheManager getEhCacheManager() {
        EhCacheManager em = new EhCacheManager();
        em.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        return em;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    @Bean(name = "securityManager")
    //配置核心安全管理器
    public DefaultWebSecurityManager getDefaultWebSecurityManager() {
        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();

        MyRealmAuthenticator authenticator =
                new MyRealmAuthenticator();
        //authenticator.setAuthenticationStrategy(new AllSuccessfulStrategy());
        //authenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());

//        FirstSuccessfulStrategy  只要有一个Realm验证成功即可，只返回第一个Realm身份验证成功的认证信息，其他的忽略；
//       (默认) AtLeastOneSuccessfulStrategy 只要有一个Realm验证成功即可，和FirstSuccessfulStrategy不同，返回所有Realm身份验证成功的认证信息；
//        AllSuccessfulStrategy 所有Realm验证成功才算成功，且返回所有Realm身份验证成功的认证信息，如果有一个失败就失败了。
        //配置权限登录器
       // dwsm.setRealm(getShiroRealm());

        Collection<Realm> realms =
                new ArrayList<>();

        realms.add(getShiroRealm());
        realms.add(getShiroRealm1()); //realm调用顺序 realms 指定的顺序   ==》 shiroRealm  shiroRealm1
        realms.add(getShiroAdmRealm());
        authenticator.setRealms(realms);
        dwsm.setAuthenticator(authenticator);
        dwsm.setCacheManager(getEhCacheManager());
        return dwsm;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(getDefaultWebSecurityManager());
        return new AuthorizationAttributeSourceAdvisor();
    }

    @Bean(name = "shiroFilter")
    /*
        ShiroFilterFactoryBean 处理拦截资源文件
        需要注入安全管理器
     */
    public ShiroFilterFactoryBean getShiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器  必须设置 securityManager
        shiroFilterFactoryBean
                .setSecurityManager(getDefaultWebSecurityManager());
        //登录的url,如果不设置会自动寻找web工程根目录下的/login.jsp页面
        shiroFilterFactoryBean.setLoginUrl("/login");
        //登录成功跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/main");
        //未授权的页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");

        //配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/static/**", "anon");//可以匿名访问

        // 配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/logout", "logout");

        // <!-- 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        filterChainDefinitionMap.put("/**", "authc");
        shiroFilterFactoryBean
                .setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }
}
