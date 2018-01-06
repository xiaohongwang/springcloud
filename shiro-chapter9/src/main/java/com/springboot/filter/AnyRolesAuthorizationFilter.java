package com.springboot.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Set;

/**
 * 自定义拦截器：用户拥有allRole中的一种角色即可访问
 *
 * @author wangxh
 * @create 2018 01 05
 */
public class AnyRolesAuthorizationFilter extends AuthorizationFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        Subject subject = this.getSubject(servletRequest, servletResponse);
        String[] rolesArray = (String[])((String[])o);
        if (rolesArray != null && rolesArray.length != 0) {
            Set<String> roles = CollectionUtils.asSet(rolesArray);

            for (String role : roles){
                if (subject.hasRole(role)){
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }
}
