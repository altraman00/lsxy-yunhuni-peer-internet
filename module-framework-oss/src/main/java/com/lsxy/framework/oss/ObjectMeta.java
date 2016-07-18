package com.lsxy.framework.oss;

import sun.security.util.Length;

/**
 * Created by Tandy on 2016/7/15.
 * OSS管理对象的元数据配置
 */
public class ObjectMeta {
    //网页下载时的文件名称
    private String contentDisposition;
    //内容类型
    private String contentType;

    //内容长度
    private String contentLength;
    //内容编码方式
    private String contentEncoding;
}
