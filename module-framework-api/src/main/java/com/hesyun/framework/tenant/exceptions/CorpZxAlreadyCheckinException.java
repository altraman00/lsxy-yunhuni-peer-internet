package com.hesyun.framework.tenant.exceptions;

import com.hesyun.framework.tenant.model.Account;

public class CorpZxAlreadyCheckinException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CorpZxAlreadyCheckinException(Account person){
        super("商户"+person.getTenant().getTenantName()+"用户："+person.getUserName()+"已在企业号签到。");
    }
}
