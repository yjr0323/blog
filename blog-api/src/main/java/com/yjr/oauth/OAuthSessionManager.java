package com.yjr.oauth;

import com.yjr.common.util.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;


public class OAuthSessionManager extends DefaultWebSessionManager {

    public static final String OAUTH_TOKEN = "Oauth-Token";

    public final static String HEADER_TOKEN_NAME = "token";

    private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";


    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Serializable sessionId = getSessionId(sessionKey);
        ServletRequest request = null;
        if(sessionId instanceof WebSessionKey){
            request = ((WebSessionKey)sessionKey).getServletRequest();
        }
        if(request != null && sessionId != null){
            return (Session) request.getAttribute(sessionId.toString());
        }

        Session session = super.retrieveSession(sessionKey);
        if(request !=null && sessionId != null){
            request.setAttribute(sessionId.toString(),session);
        }
        return session;
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String id = httpRequest.getHeader(OAUTH_TOKEN);
        if (!StringUtils.isEmpty(id)){
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
        }else {
            return super.getSessionId(request,response);
        }

        return id;
    }


}

