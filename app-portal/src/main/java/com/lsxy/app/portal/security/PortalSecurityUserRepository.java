package com.lsxy.app.portal.security;

import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.core.security.SecurityUser;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
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
    public SecurityUser loadSecurityUser(String token,String userName) {
        if(logger.isDebugEnabled()){
            logger.debug(String.format("load security user in %s",userName));
        }
        return getUser(token,userName);
    }

    /**
     * 根据用户名获取用户信息
     * @return
     */
    public SecurityUser getUser(String token,String userName){
        String uri = PortalConstants.REST_PREFIX_URL +   "/rest/account/user/" + userName;
        RestResponse<SecurityUser> restResponse = RestRequest.buildSecurityRequest(token).get(uri, SecurityUser.class);
        SecurityUser user = restResponse.getData();
        return user;
    }

}
