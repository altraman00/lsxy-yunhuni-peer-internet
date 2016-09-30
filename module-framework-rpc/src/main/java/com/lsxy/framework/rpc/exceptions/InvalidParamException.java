package com.lsxy.framework.rpc.exceptions;

/**
 * Created by liuws on 2016/9/30.
 */
public class InvalidParamException extends RuntimeException{

    public InvalidParamException() {
        super();
    }

    public InvalidParamException(String message) {
        super(message);
    }

    public InvalidParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidParamException(Throwable cause) {
        super(cause);
    }

    public InvalidParamException(String format,Object... args){
        super(String.format(format,args));
    }
}
