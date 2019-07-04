package com.itsu.springbootshiro.freemarker;

import com.jagregory.shiro.freemarker.ShiroTags;
import freemarker.template.Configuration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 苏犇
 * @date 2019/7/4 23:47
 */
@Component
public class CustomFreemarkerConfigurer implements InitializingBean {

    @Resource
    private Configuration configuration;

    @Override
    public void afterPropertiesSet() throws Exception {
        configuration.setSharedVariable("shiro",new ShiroTags());
    }
}
