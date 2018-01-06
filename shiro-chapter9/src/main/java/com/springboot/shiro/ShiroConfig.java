package com.springboot.shiro;

import com.springboot.filter.AnyRolesAuthorizationFilter;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.permission.WildcardPermissionResolver;
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

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    //拦截器
    private static Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

    @Bean(name = "lifecycleBeanPostProcessor")
    /**管理Shiro的生命周期，初始化和销毁*/
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public HashedCredentialsMatcher createCredentialsMatcher(){
        //自定义比较器
        HashedCredentialsMatcher matcher =
                new RetryLimitHashedCredential(getEhCacheManager());
        //散列算法
        matcher.setHashAlgorithmName("md5");
        //散列迭代次数
        matcher.setHashIterations(2);
        //表示是否存储散列后的密码为16进制，需要和生成密码时的一样，默认是base64
        matcher.setStoredCredentialsHexEncoded(true);
        //
        matcher.setHashSalted(true);
        return matcher;
    }

    @Bean("shiroRealm")
    /**配置权限登录器*/
    public Realm getShiroRealm(){
        AuthorizingRealm realm =  new MyRealm();
        realm.setCredentialsMatcher(createCredentialsMatcher());
        return realm;
    }

    @Bean(name = "shiroEhcacheManager")
    /**
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
    /**配置核心安全管理器*/
    public DefaultWebSecurityManager getDefaultWebSecurityManager() {
        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();

        ModularRealmAuthorizer authorizer =
                    new ModularRealmAuthorizer();

        authorizer.setRolePermissionResolver(new MyRolePermissionResolver());
        authorizer.setPermissionResolver(new WildcardPermissionResolver());
        dwsm.setAuthorizer(authorizer);

        //配置权限登录器
        dwsm.setRealm(getShiroRealm());
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
    /**
        ShiroFilterFactoryBean 处理拦截资源文件
        需要注入安全管理器
     */
    public ShiroFilterFactoryBean getShiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器  必须设置 securityManager
        shiroFilterFactoryBean
                .setSecurityManager(getDefaultWebSecurityManager());

        AnyRolesAuthorizationFilter anyRole =
                new AnyRolesAuthorizationFilter();
        Map<String, Filter> filterMap =
                new HashMap<>();
        filterMap.put("anyRole", anyRole);
        shiroFilterFactoryBean.setFilters(filterMap);

        //登录的url,如果不设置会自动寻找web工程根目录下的/login.jsp页面
        shiroFilterFactoryBean.setLoginUrl("/login/**");
        //登录成功跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/main");
        //未授权的页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");

        //RolesAuthorizationFilter  同时拥有role 与 sysUser 角色时可访问/testRoleFilter
        filterChainDefinitionMap.put("/testRoleFilter", "roles[role,sysUser]");

        filterChainDefinitionMap.put("/testAnyRoleFilter", "anyRole[admin,sysUser]");

        //配置不会被拦截的链接 顺序判断 //可以匿名访问
        filterChainDefinitionMap.put("/static/**", "anon");

        // 配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/logout", "logout");

        // <!-- 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        //FormAuthenticationFilter
        filterChainDefinitionMap.put("/**", "authc");
        shiroFilterFactoryBean
                .setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }
}
