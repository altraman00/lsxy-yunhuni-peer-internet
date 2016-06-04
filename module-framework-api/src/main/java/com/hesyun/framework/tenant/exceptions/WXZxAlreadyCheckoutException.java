package com.hesyun.framework.tenant.exceptions;

public class WXZxAlreadyCheckoutException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public WXZxAlreadyCheckoutException(String username){
        super("用户："+username+"还没有在服务号签到，无法签出。");
    }
}
