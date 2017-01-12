package com.lsxy.app.mc.exceptions;

/**
 * Created by tandy on 17/1/12.
 */
public class ScriptFileNotExistException extends Throwable {
    public ScriptFileNotExistException(String scriptName) {
        super("script file not exist : "+ scriptName);
    }
}
