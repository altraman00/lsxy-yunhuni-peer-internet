package com.oneyun.sapi;

import com.oneyun.sapi.utils.DateUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhangxb on 2017/1/16.
 */
public class MyLogger {
    private  String getTime(){
        String timestr;
        String  parrten = "yyyy-MM-dd HH:mm:ss sss";
        SimpleDateFormat sdf = new SimpleDateFormat(parrten);
        Date day = new Date();
        timestr = sdf.format(day);
        return timestr;
    }
    public  void info(String info){
//        String method = Thread.currentThread() .getStackTrace()[1].getMethodName();
//        System.out.println(method);
        StackTraceElement[] stack = (new Throwable()).getStackTrace();
        String d = "";
        if(stack.length>=2){
            StackTraceElement ste = stack[1];
            d = ste.getClassName()   + "." +   ste.getMethodName() ;
//            System.out.println(ste.getMethodName());
//            System.out.println(ste.getFileName());
        }
        String str = getTime() +"\t"+ d +"\t"+ info;
        System.out.println( str );
    }
}
