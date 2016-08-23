package com.lsxy.area.api.exceptions;

/**
 * Created by liups on 2016/8/23.
 */
public class AppServiceNotOn extends RuntimeException {
    public AppServiceNotOn(String message) {
        super(message);
    }
}
