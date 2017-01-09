package com.lsxy.framework.core.utils;

import org.apache.commons.httpclient.HttpURL;
import org.apache.commons.httpclient.HttpsURL;
import org.apache.commons.httpclient.URIException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

/**
 * Created by liuws on 2017/1/9.
 */
public final class UrlUtils {

    private static final Pattern ABSOLUTE_URL = Pattern.compile("\\A[a-z0-9.+-]+://.*", 2);

    private UrlUtils() {
    }

    public static String buildFullRequestUrl(String scheme, String serverName, int serverPort, String requestURI, String queryString) {
        scheme = scheme.toLowerCase();
        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);
        if("http".equals(scheme)) {
            if(serverPort != 80) {
                url.append(":").append(serverPort);
            }
        } else if("https".equals(scheme) && serverPort != 443) {
            url.append(":").append(serverPort);
        }

        url.append(requestURI);
        if(queryString != null) {
            url.append("?").append(queryString);
        }

        return url.toString();
    }

    public static boolean isAbsoluteUrl(String url) {
        return ABSOLUTE_URL.matcher(url).matches();
    }

    /***
     * 根据base（当前路径）和innerUrl（相对路径）获取绝对路径
     *
     * @param base
     * @param relative
     * @return
     */
    public static String resolve(String base,String relative) {
        if(relative == null){
            throw new IllegalArgumentException(relative);
        }
        if(isAbsoluteUrl(relative)){//如果是绝对路径直接返回
            return relative;
        }
        if(StringUtil.isBlank(base)){
            throw new IllegalArgumentException(base);
        }

        String schema = null;
        try {
            schema = new URI(base).getScheme().toLowerCase();
        } catch (Throwable e) {
            throw new IllegalArgumentException(base+"==="+relative,e);
        }
        if(schema.equals("https")){
            try {
                return new HttpsURL(new HttpsURL(base),relative).toString();
            } catch (Throwable e) {
                throw new IllegalArgumentException(base+"==="+relative,e);
            }
        }else{
            try {
                return new HttpURL(new HttpURL(base),relative).toString();
            } catch (Throwable e) {
                throw new IllegalArgumentException(base+"==="+relative,e);
            }
        }
    }
}
