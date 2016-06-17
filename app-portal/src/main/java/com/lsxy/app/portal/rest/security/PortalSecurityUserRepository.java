package com.lsxy.app.portal.rest.security;

import com.lsxy.framework.core.security.SecurityUser;
import com.lsxy.framework.web.security.SecurityUserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

/**
 * Created by Tandy on 2016/6/8.
 */
@Component
public class PortalSecurityUserRepository implements SecurityUserRepository{

    private static  final Log logger = LogFactory.getLog(PortalSecurityUserRepository.class);

    @Override
    public SecurityUser loadSecurityUser(String userName) {
        if(logger.isDebugEnabled()){
            logger.debug(String.format("load security user in %s",userName));
        }
        return new SecurityUser("1234","user","用户001","user","user");
    }
}
