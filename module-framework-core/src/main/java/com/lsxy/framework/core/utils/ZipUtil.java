package com.lsxy.framework.core.utils;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 压缩文件成zip
 * Created by zhangxb on 2016/7/23.
 */
public class ZipUtil {
    private static final Project DEFAULT_PROJECT = new Project();

    public static void unZip(File orgin, File dest) {
        Expand expand = new Expand();
        expand.setProject(DEFAULT_PROJECT);
        expand.setSrc(orgin);
        expand.setDest(dest);
        expand.execute();
    }

    /**
     * 生成压缩文件
     * @param orgins 文件集合 全地址
     * @param dest 生成集合文件 全地址
     */
    public static void zip(List<String> orgins, String dest) {
        Zip zip = new Zip();
        zip.setProject(DEFAULT_PROJECT);
        File destFile = new File(dest);
        zip.setDestFile(destFile);

        FileSet fs = new FileSet();
        fs.setProject(DEFAULT_PROJECT);
        // 判断是目录还是文件
        for(int i=0;i<orgins.size();i++){
            File tmep = new File(orgins.get(i));
            if (tmep.isDirectory()) {
                fs.setDir(tmep);//文件夹
            } else {
                fs.setFile(tmep);//文件
            }
        }
        zip.addFileset(fs);
        zip.execute();

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
//        File orgin = new File("f:\\PID_3C99008722CF3A4DC8EBE8FAA267FDE9F11E.zip");
        File dest = new File("f:\\1\\1.jpg");
//        unZip(orgin, dest);
//        System.out.println("----------un zip -----------");
        List<String> list = new ArrayList();
        list.add("f:\\1\\1.jpg");
        list.add("f:\\1\\2.jpg");
        list.add("f:\\1\\3.jpg");
        list.add("f:\\1\\4.jpg");
        list.add("f:\\1\\5.jpg");
        list.add("f:\\1\\6.jpg");
        list.add("f:\\1\\7.jpg");
        System.out.println("----------zip starting-----------");
        zip(list, "f:\\zip\\test.zip");
        System.out.println("----------zip success-----------");
    }
}
