package com.lsxy.framework.core.exceptions;

/**
 * Created by Tandy on 2016/6/25.
 */
public class LsxyRuntimeException extends Exception{
    private String errorCode;
    private String errorMsg;
    private Object[] ctxParams;

//    public LsxyRuntimeException(AllErrors error){
//        super(error.toString());
//        this.errorCode = error.getErrorCode();
//        this.errorMsg = error.getErrorMsg();
//
//    }

    public void params(Object ... params){
        this.ctxParams = params;
    }

}
