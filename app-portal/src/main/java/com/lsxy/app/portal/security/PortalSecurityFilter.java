package com.lsxy.app.portal.security;

import com.lsxy.framework.core.web.SpringContextUtil;
import com.lsxy.framework.web.security.SecurityFilter;
import com.lsxy.framework.web.security.SecurityUserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.annotation.WebFilter;

/**
 * Created by Tandy on 2016/6/8.
 */
@WebFilter(filterName = "securityFilter", urlPatterns = "/console/*",
        asyncSupported = true //支持异步Servlet
)
public class PortalSecurityFilter extends SecurityFilter {
    private static final Log logger = LogFactory.getLog(PortalSecurityFilter.class);

    @Override
    public SecurityUserRepository getSecurityUserRepository() {
        return SpringContextUtil.getApplicationContext().getBean(SecurityUserRepository.class);
    }
}
