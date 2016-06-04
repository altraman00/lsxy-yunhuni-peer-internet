package com.hesyun.framework.tenant.exceptions;

public class CorpZxAlreadyCheckoutException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CorpZxAlreadyCheckoutException(String username){
        super("用户："+username+"还没有在企业号签到，无法签出。");
    }
}
