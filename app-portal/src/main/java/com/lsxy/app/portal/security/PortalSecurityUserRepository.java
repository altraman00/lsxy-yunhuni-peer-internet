package com.lsxy.app.portal.security;

import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.api.tenant.model.Account;
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
    public SecurityUser loadSecurityUser(String token) {
        if(logger.isDebugEnabled()){
            logger.debug(String.format("load security user"));
        }
        return getUser(token);
    }

    /**
     * 根据用户名获取用户信息
     * @return
     */
    public SecurityUser getUser(String token){
        String uri = PortalConstants.REST_PREFIX_URL +  "/rest/account/get/current";
        RestResponse<Account> restResponse = RestRequest.buildSecurityRequest(token).get(uri, Account.class);
        Account account = restResponse.getData();
        return new SecurityUser(account.getId(),account.getUserName(),account.getTenant().getTenantUid(),account.getTenant().getId());
    }

}
