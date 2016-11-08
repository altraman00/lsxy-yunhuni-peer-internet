package com.lsxy.call.center.test;

import com.lsxy.call.center.utils.ExpressionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuws on 2016/11/8.
 */
public class ExpressionTest {

    public static void main(String[] args) {

        Map<String,Integer> vars = new HashMap<>();
        System.out.println(ExpressionUtils.invalidSortExpression("get(\"haha0\") + get(\"haha1\");"));;
    }


}
