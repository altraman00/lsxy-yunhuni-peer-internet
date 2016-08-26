package com.lsxy.area.api.exceptions;

/**
 * Created by tandy on 16/8/18.
 */
public class InvokeCallException extends YunhuniApiException {
    public InvokeCallException(Exception ex) {
        super(ex);
    }

    public InvokeCallException(String s) {
        super(s);
    }

    @Override
    public String getCode() {
        return "101012";
    }
}
