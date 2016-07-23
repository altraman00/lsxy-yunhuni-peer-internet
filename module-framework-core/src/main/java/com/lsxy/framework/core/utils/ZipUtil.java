package com.lsxy.framework.core.utils;

import java.io.File;

/**
 * 压缩文件成zip
 * Created by zhangxb on 2016/7/23.
 */
public class ZipUtil {
    //private static final Project DEFAULT_PROJECT = new Project();

    public static void unZip(File orgin, File dest) {

//        Expand expand = new Expand();
//        expand.setProject(DEFAULT_PROJECT);
//        expand.setSrc(orgin);
//        expand.setDest(dest);
//        expand.execute();

    }

    public static void zip(File orgin, File dest) {

//        Zip zip = new Zip();
//        zip.setProject(DEFAULT_PROJECT);
//        zip.setDestFile(dest);
//
//        FileSet fs = new FileSet();
//        fs.setProject(DEFAULT_PROJECT);
//        fs.setDir(orgin);
////		fs.setIncludes("**/*.java");
////		fs.setExcludes("**/*.xml");
//
//        zip.addFileset(fs);
//        zip.execute();

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        File orgin = new File("f:\\PID_3C99008722CF3A4DC8EBE8FAA267FDE9F11E.zip");
        File dest = new File("f:\\unzip\\");
        unZip(orgin, dest);
        System.out.println("----------un zip -----------");

        File zip = new File("f:\\zip\\test.zip");
        System.out.println("----------zip starting-----------");
        zip(dest, zip);
        System.out.println("----------zip success-----------");
    }
}
