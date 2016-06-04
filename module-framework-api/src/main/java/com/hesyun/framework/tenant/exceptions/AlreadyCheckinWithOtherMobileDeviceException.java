package com.hesyun.framework.tenant.exceptions;

import com.hesyun.framework.tenant.model.Account;

public class AlreadyCheckinWithOtherMobileDeviceException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AlreadyCheckinWithOtherMobileDeviceException(Account person){
        super("商户"+person.getTenant().getTenantName()+"用户："+person.getUserName()+"已在其他移动端签到。");
    }
}
