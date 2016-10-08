package com.lsxy.framework.core.utils;

import org.apache.poi.hssf.usermodel.*;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * 导出文件
 * Created by zhangxb on 2016/10/8.
 */
public class ExportExcel   {
    /**
     * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
     * @param title 表格标题名
     * @param headers 表格属性列名数组
     * @param values 为空时安装bean对象字段顺序取值
     * @param dataset
     *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
     *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
     * @param out 与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     * @param pattern 如果有时间数据，设定输出格式。默认为"yyy-MM-dd HH:mm:ss"
     */
    public static <T> void exportExcel(String title, String[] headers, String[] values,Collection<T> dataset, OutputStream out, String pattern) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        if(pattern==null){
            pattern = "yyy-MM-dd HH:mm:ss";
        }
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 20);

        //生成字体
        HSSFFont font = workbook.createFont();
        //设置颜色
//        font.setColor(HSSFFont.COLOR_RED);
        //设置加粗
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 生成一个样式
        HSSFCellStyle style= workbook.createCellStyle();
        //将字体添加到样式中
        style.setFont(font);

        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        for (short i = 0; i < headers.length; i++)
        {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        int index = 0;
        while (it.hasNext())
        {
            index++;
            row = sheet.createRow(index);
            T t = (T) it.next();
            // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
            String[] objs = null;
            if(values !=null&&values.length>0){
                objs = values;
            }else{
                Field[] fields = t.getClass().getDeclaredFields();
                objs = new String[fields.length];
                for(int i=0;i<fields.length;i++){
                    objs[i]=fields[i].getName();
                }
            }
            for (short i = 0; i < objs.length; i++)
            {
                HSSFCell cell = row.createCell(i);
//                cell.setCellStyle(style2);
                String fieldName = objs[i];
                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Class tCls = t.getClass();
                Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
                Object value = getMethod.invoke(t, new Object[]{});
                // 判断值的类型后进行强制类型转换
                String textValue = null;
               if(value instanceof Date){
                    Date date = (Date) value;
                    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                    textValue = sdf.format(date);
                }else{
                    //其它数据类型都当作字符串简单处理
                    textValue = value.toString();
                }
                if(textValue != null){
                    cell.setCellValue(textValue);
                }
            }
        }
        workbook.write(out);
    }
}
