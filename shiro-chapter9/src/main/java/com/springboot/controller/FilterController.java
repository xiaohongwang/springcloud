package com.springboot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 拦截器配置
 *
 * @author wangxh
 * @create 2018 01 05
 */
@RestController
public class FilterController {

    private static final Logger log =
                LoggerFactory.getLogger(FilterController.class);

    @RequestMapping("/testRoleFilter")
    public String testRoleFilter(){
        log.info("角色拦截器配置");
        return "成功访问";
    }


    @RequestMapping("/testAnyRoleFilter")
    public String testAnyRoleFilter(){
        log.info("角色拦截器配置");
        return "成功访问";
    }
}
