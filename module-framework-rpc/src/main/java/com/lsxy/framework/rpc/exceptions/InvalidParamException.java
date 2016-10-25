package com.lsxy.framework.rpc.exceptions;

import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.core.utils.StringUtil;

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
        super(format(format,args));
    }

    private static String format(String format,Object... args){
        if(StringUtil.isBlank(format)){
            return format;
        }
        format = format.replaceAll("\\{\\}","%s");
        for (int i =0,len=args.length;i<len;i++) {
            Object obj = args[i];
            if(obj instanceof String){
                continue;
            }
            args[i] = JSONUtil.objectToJson(obj);
        }
        return String.format(format,args);
    }
}
