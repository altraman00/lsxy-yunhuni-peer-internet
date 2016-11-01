package com.lsxy.framework.core.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhangxb on 2016/10/27.
 */
public class ExcelOperate {
    public static void main(String[] args) {
        List list = readXml("e:/telnum.xlsx");
        System.out.println(list);
    }
    public static List readXml(String fileName){
        List list = new ArrayList();
        boolean isE2007 = false;    //判断是否是excel2007格式
        if(fileName.endsWith("xlsx"))
            isE2007 = true;
        try {
            InputStream input = new FileInputStream(fileName);  //建立输入流
            Workbook wb  = null;
            //根据文件格式(2003或者2007)来初始化
            if(isE2007)
                wb = new XSSFWorkbook(input);
            else
                wb = new HSSFWorkbook(input);
            Sheet sheet = wb.getSheetAt(0);     //获得第一个表单
            Iterator<Row> rows = sheet.rowIterator(); //获得第一个表单的迭代器
            while (rows.hasNext()) {
                Row row = rows.next();  //获得行数据
//                System.out.println("Row #" + row.getRowNum());  //获得行号从0开始
                Iterator<Cell> cells = row.cellIterator();    //获得第一行的迭代器
                ArrayList list1 = new ArrayList();
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    Object o = null;
                    switch (cell.getCellType()) {   //根据cell中的类型来输出数据
                        case HSSFCell.CELL_TYPE_NUMERIC:
                            o = cell.getNumericCellValue();
                            break;
                        case HSSFCell.CELL_TYPE_STRING:
                            o = cell.getStringCellValue();
                            break;
                        case HSSFCell.CELL_TYPE_BOOLEAN:
                            o = cell.getBooleanCellValue();
                            break;
                        case HSSFCell.CELL_TYPE_FORMULA:
                            o = cell.getCellFormula();
                            break;
                        default:
                            o = "";
                            break;
                    }
                    list1.add(o);
                }
                list.add(list1);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return list;
    }

}
