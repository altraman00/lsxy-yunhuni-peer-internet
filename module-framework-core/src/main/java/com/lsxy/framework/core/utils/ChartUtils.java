package com.lsxy.framework.core.utils;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by zhangxb on 2016/10/24.
 */
public class ChartUtils {
    public static  <T> Object[] getArrays(Collection<T> dataset, Object leng, String key,String values,String rule){
        //获取数组长度
        int length = 0;
        if (leng instanceof Date) {
            length = Integer.valueOf(DateUtils.getLastDate((Date)leng).split("-")[2]);
        } else if (leng instanceof Integer) {
            length =Integer.valueOf((Integer)leng);
        }
        //创建数组并初始化
        Object[] list = new Object[length];
        for(int j=0;j<length;j++){
            list[j]=0;
        }
        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        while (it.hasNext())
        {
            T t = (T) it.next();
            try {
                Object obj1 = getValue(t, key);
                if(obj1 instanceof Integer){
                    int tempObj1 = (Integer)obj1;
                    Object obj2 = getValue(t,values);
                    if(StringUtils.isNotEmpty(rule)){
                        if(rule.indexOf("/")==0){
                            long tempObj2 = Long.valueOf(obj2.toString());
                            int divisor = Integer.valueOf(rule.substring(1,rule.length()));
                            list[tempObj1-1] = tempObj2/divisor;
                        }else if(rule.indexOf(".")==0){
                            int digit = Integer.valueOf(rule.substring(1,rule.length()));
                            list[tempObj1-1] = getDecimal(obj2.toString(),digit);
                        }
                    }else{
                        list[tempObj1-1] = obj2;
                    }
                }
            }catch (Exception e){};
        }
        return list;
    }
    private static <T> Object getValue( T t, String fieldName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Class tCls = t.getClass();
        Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
        Object value = getMethod.invoke(t, new Object[]{});
        return value;
    }
    public static String getDecimal(String o,int num){
        String[] temp = o.split("\\.");
        String result = temp[0];
        if(temp.length>1){
            String re2 = temp[1];
            if(re2.length()>num){
                re2 = re2.substring(0,3);
            }else if(re2.length()<num){
                int tleng = num-re2.length();
                for(int i=0;i<tleng;i++){
                    re2+="0";
                }
            }
            result+="."+re2;
        }else if(temp.length==1){
            result+=".";
            for(int i=0;i<num;i++){
                result+="0";
            }
        }
        return result;
    }
    public static void main(String []args){
//        List list = new ArrayList();
//        for(int i=0;i<12;i++){
//            VoiceCdrDay voiceCdrDay = new VoiceCdrDay();
//            voiceCdrDay.setDay(i+1);
//            Random random = new Random();
//            voiceCdrDay.setAmongCostTime(Long.valueOf(random.nextInt(10000)+""));
//            list.add(voiceCdrDay);
//        }
//        Object[] obj =  getArrays(list,12,"day","amongCostTime",".3");
//        System.out.println(Arrays.toString(obj));
    }
}
