package com.yjr.config;


import com.yjr.common.cache.RedisManager;
import com.yjr.oauth.OAuthRealm;
import com.yjr.oauth.OAuthSessionDAO;
import com.yjr.oauth.OAuthSessionManager;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;



@Configuration
public class ShiroConfig {




    @Bean
        public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //必须设置securityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();  
       /* filterChainDefinitionMap.put("/", "anon");  


        //anon:所有url都可以匿名访问
        filterChainDefinitionMap.put("/static/**", "anon"); 
        filterChainDefinitionMap.put("/blogFile/**", "anon");
        filterChainDefinitionMap.put("/login", "anon"); 
        filterChainDefinitionMap.put("/register", "anon"); 
        filterChainDefinitionMap.put("/**", "authc"); */
//        filterChainDefinitionMap.put("/logout","logout");
       //authc:所有url必须通过认证才能访问
        //filterChainDefinitionMap.put("/**/create", "authc");
        //filterChainDefinitionMap.put("/**/update", "authc");
        //filterChainDefinitionMap.put("/**/delete", "authc");
        //filterChainDefinitionMap.put("/upload", "authc");
        //filterChainDefinitionMap.put("/users/currentUser", "authc");
        filterChainDefinitionMap.put("/**", "anon");

        //返回json数据，由前端跳转(登录页面)  setSuccessUrl:登录成功页面  setUnauthorizedUrl:未授权页面
        shiroFilterFactoryBean.setLoginUrl("/handleLogin");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }


    /**
     * 凭证匹配器
     * 密码交给了SimpleAuthenticationInfo进行处理了
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //散列算法：这里用的是md5
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        //散列的次数：md5(md5(""))
        hashedCredentialsMatcher.setHashIterations(2);
        return hashedCredentialsMatcher;
    }

    @Bean
    public OAuthRealm oAuthRealm() {
        OAuthRealm myShiroRealm = new OAuthRealm();
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }


    @Bean
    public SecurityManager securityManager(OAuthRealm oAuthRealm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(oAuthRealm);
        securityManager.setSessionManager(sessionManager);
        // 自定义缓存实现 使用redis  
        //securityManager.setCacheManager(cacheManager());  
        return securityManager;
    }

    @Bean
    public SessionManager sessionManager(OAuthSessionDAO authSessionDAO) {
        OAuthSessionManager oAuthSessionManager = new OAuthSessionManager();
        oAuthSessionManager.setSessionDAO(authSessionDAO);
        oAuthSessionManager.setSessionIdUrlRewritingEnabled(false);

        oAuthSessionManager.setSessionIdCookieEnabled(true);
        oAuthSessionManager.getSessionIdCookie().setName("abc");
        oAuthSessionManager.getSessionIdCookie().setMaxAge(60 * 60 * 1 * 1);
        oAuthSessionManager.getSessionIdCookie().setHttpOnly(true);
        return oAuthSessionManager;
    }


    @Bean
    public OAuthSessionDAO authSessionDAO(RedisManager redisManager) {
        OAuthSessionDAO authSessionDAO = new OAuthSessionDAO();
        authSessionDAO.setRedisManager(redisManager);
        return authSessionDAO;
    }


    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
