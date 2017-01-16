package com.lsxy.app.mc.utils;

/**
 * Created by tandy on 17/1/6.
 */
public class ShellExecuteException extends Throwable {
    public ShellExecuteException(String scriptFilePath, Exception e) {
        super("执行脚本出现异常:"+scriptFilePath);
    }
}
