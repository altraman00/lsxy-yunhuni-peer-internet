package com.lsxy.area.api.exceptions;

/**
 * Created by liups on 2016/8/26.
 */
public abstract class YunhuniApiException extends Exception{
    public YunhuniApiException(Exception ex) {
        super(ex);
    }

    public YunhuniApiException(String message) {
        super(message);
    }

    public abstract String getCode();

}
