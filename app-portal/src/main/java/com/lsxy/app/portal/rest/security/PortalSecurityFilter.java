package com.lsxy.app.portal.rest.security;

import com.lsxy.framework.core.web.SpringContextUtil;
import com.lsxy.framework.web.security.SecurityFilter;
import com.lsxy.framework.web.security.SecurityUserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Created by Tandy on 2016/6/8.
 */
@WebFilter(filterName = "securityFilter", urlPatterns = "/*")
public class PortalSecurityFilter extends SecurityFilter {
    private static final Log logger = LogFactory.getLog(PortalSecurityFilter.class);

    @Override
    public SecurityUserRepository getSecurityUserRepository() {
        return SpringContextUtil.getApplicationContext().getBean(SecurityUserRepository.class);
    }
}
