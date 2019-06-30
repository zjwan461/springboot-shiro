package com.itsu.springbootshiro;

import com.itsu.springbootshiro.shiro.cache.RedisCacheManager;
import com.itsu.springbootshiro.shiro.realm.CustomRealm;
import com.itsu.springbootshiro.shiro.sessiondao.RedisSessionDao;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 苏犇
 * @date 2019/6/27 23:28
 */
@Configuration
public class ShiroConfig {

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 开启shiro注解
     *
     * @param
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(initSecurityManager());
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public ShiroFilterFactoryBean initShiroFilterFactory() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setUnauthorizedUrl("/error/500");
        shiroFilterFactoryBean.setSecurityManager(initSecurityManager());
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        //注意过滤器配置顺序 不能颠倒
        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了，登出后跳转配置的loginUrl
        filterChainDefinitionMap.put("/logout", "logout");
        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/login.do", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/", "user");
        filterChainDefinitionMap.put("/**", "authc");
        //配置shiro默认登录界面地址，前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据
        shiroFilterFactoryBean.setLoginUrl("/login");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public DefaultWebSecurityManager initSecurityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(initCustomRealm());
        securityManager.setSessionManager(initSessionManager());
//        securityManager.setCacheManager(initCacheManager());
        securityManager.setCacheManager(initRedisCacheManager());
        securityManager.setRememberMeManager(initRemeberMeManager());
        return securityManager;
    }

    @Bean
    public CookieRememberMeManager initRemeberMeManager() {
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        rememberMeManager.setCookie(initSimpleCookie());
        return rememberMeManager;
    }

    @Bean
    public SimpleCookie initSimpleCookie() {
        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setMaxAge(20000);
        simpleCookie.setName("rememberMe");
        return simpleCookie;
    }

    @Bean
    public CustomRealm initCustomRealm() {
        CustomRealm customRealm = new CustomRealm();
        customRealm.setName("customRealm");
        customRealm.setAuthorizationCachingEnabled(true);
        customRealm.setAuthenticationCachingEnabled(true);
        customRealm.setCredentialsMatcher(initCredentialsMatcher());
        return customRealm;
    }

    @Bean
    public HashedCredentialsMatcher initCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("MD5");
        credentialsMatcher.setHashIterations(1);
        return credentialsMatcher;
    }

    @Bean
    public DefaultWebSessionManager initSessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
//        sessionManager.setSessionDAO(initSessionDao());
        sessionManager.setSessionDAO(initRedisSessionDao());
        sessionManager.setGlobalSessionTimeout(360000);
        sessionManager.setDeleteInvalidSessions(true);
        return sessionManager;
    }

    /**
     * 内存存储session，底层就是一个ConcurrentHashMap
     * 也可以自己实现。 如果不想使用redis 存储session，可以用这个sessionDAO
     * @return
     */
    @Bean(name = "memorySessionDao")
    public MemorySessionDAO initSessionDao() {
        MemorySessionDAO sessionDAO = new MemorySessionDAO();
        return sessionDAO;
    }

    @Bean(name = "RedisSessionDao")
    public RedisSessionDao initRedisSessionDao() {
        RedisSessionDao redisSessionDao = new RedisSessionDao();
        return redisSessionDao;
    }

    /**
     * 内存缓存，底层就是一个ConcurrentHashMap
     * 也可以自己实现。如果不想使用redis 存储cache，可以用这个cachemanager
     * @return
     */
    @Bean
    public MemoryConstrainedCacheManager initCacheManager() {
        MemoryConstrainedCacheManager cacheManager = new MemoryConstrainedCacheManager();
        return cacheManager;
    }

    @Bean
    public RedisCacheManager initRedisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        return redisCacheManager;
    }


}
