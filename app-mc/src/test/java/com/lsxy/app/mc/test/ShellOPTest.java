package com.lsxy.app.mc.test;

import com.lsxy.app.mc.utils.RunShellUtil;
import com.lsxy.app.mc.utils.ShellExecuteException;

import java.util.Scanner;

/**
 * Created by tandy on 17/1/6.
 */
public class ShellOPTest {
    public static void main(String[] args)  {
        System.out.println("hello world");
        Scanner s = new Scanner(System.in);
        String line = null;
        System.out.print("shell>");
        while((line = s.nextLine())!=null){
            String result = null;
            try {
                result = RunShellUtil.run(line,2);
            } catch (ShellExecuteException e) {
            }
            System.out.println(result);
            System.out.print("shell>");
        }
    }




}
