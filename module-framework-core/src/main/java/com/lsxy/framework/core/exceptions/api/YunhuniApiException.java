package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2016/8/26.
 */
public abstract class YunhuniApiException extends Exception{

    private String context;

    public YunhuniApiException(Throwable t) {
        super(t);
    }

    public YunhuniApiException() {
        super();
    }

    public YunhuniApiException(String context){
        super();
        this.context = context;
    }

    public YunhuniApiException(ExceptionContext context){
        super();
        if(context!=null){
            this.context = context.toString();
        }
    }

    public abstract ApiReturnCodeEnum getApiExceptionEnum();

    public final String getCode(){
        return getApiExceptionEnum().getCode();
    }

    public final String getSimpleMessage(){
        return getApiExceptionEnum().getMsg();
    }

    public final String getMessage(){
        return "[code="+getCode()+"]"+"[message="+getSimpleMessage()+"]"+"[context="+this.context+"]";
    }

}
