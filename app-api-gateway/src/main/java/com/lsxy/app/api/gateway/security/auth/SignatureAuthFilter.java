package com.lsxy.app.api.gateway.security.auth;

import com.lsxy.app.api.gateway.util.SpringContextHolder;
import com.lsxy.framework.api.gateway.model.ApiInvokeLog;
import com.lsxy.framework.api.gateway.service.ApiInvokeLogService;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Tandy on 2016/6/30.
 * 签名认证过滤器
 */
//@Component  不能打开该注释，否则会出问题
public class SignatureAuthFilter extends GenericFilterBean{


    private AuthenticationManager authenticationManager;

    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    private ASyncSaveApiLogTask saveApiLogTask;

    private static final Logger logger = LoggerFactory.getLogger(SignatureAuthFilter.class);
    // Enable Multi-Read for PUT and POST requests
    private static final Set<String> METHOD_HAS_CONTENT = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER) {
        private static final long serialVersionUID = 1L;
        { add("PUT"); add("POST"); }
    };


    public SignatureAuthFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
        this.restAuthenticationEntryPoint = new RestAuthenticationEntryPoint();

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        // use wrapper to read multiple times the content
        AuthenticationRequestWrapper request = new AuthenticationRequestWrapper((HttpServletRequest) req);
        HttpServletResponse response = (HttpServletResponse) resp;

        if (logger.isDebugEnabled()) {
            logger.debug("进入签名认证过滤器：" + request.getRequestURI());
        }
//         Get authorization header
        String credentials = request.getHeader("Authorization");
        String timestamp = request.getHeader("Timestamp");
        String appid = request.getHeader("AppID");
        String payload = request.getPayload();
        String method = request.getMethod();
        // If there's not credentials return...
        if (credentials == null || credentials.indexOf(":") < 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("没有签名认证凭证：" + request.getRequestURI());
            }
//            chain.doFilter(request, response);
            return;
        }

        // Authorization header is in the form <public_access_key>:<signature>
        String auth[] = credentials.split(":");
        String certid = auth[0];
        String signature = auth[1];
        String apiuri = request.getRequestURI();

        // 是否有post 或者 put  body
        boolean hasContent = METHOD_HAS_CONTENT.contains(request.getMethod());

        String contentMd5 = hasContent ? (new Md5PasswordEncoder()).encodePassword(payload, null) : "";
        String contentType = hasContent ? request.getContentType() : "";

        // 组织签名数据
        StringBuilder toSign = new StringBuilder();
        toSign.append(method).append("\n")
                .append(contentMd5).append("\n")
                .append(contentType).append("\n")
                .append(timestamp).append("\n")
                .append(appid).append("\n")
                .append(apiuri);

        RestCredentials restCredential = new RestCredentials(toSign.toString(), signature);
        try {
            Date date = DateUtils.parseDate(timestamp, "yyyyMMddHHmmss");


            // Create an authentication token
            Authentication authentication = new RestToken(certid, restCredential, date);

            Authentication successfulAuthentication = authenticationManager.authenticate(authentication);

            //认证成功，设置认证token
            SecurityContextHolder.getContext().setAuthentication(successfulAuthentication);

            //调用日志异步入库
            getSaveApiLogTask().invokeApiSaveDB(appid, payload, contentType, method, signature, apiuri);

            if(logger.isDebugEnabled()) {
                logger.debug("异步入库中，继续doFilter");
            }
            // Continue with the Filters
            chain.doFilter(request, response);
        } catch (AuthenticationException authenticationException) {
            SecurityContextHolder.clearContext();
            restAuthenticationEntryPoint.commence(request, response, authenticationException);
        } catch (ParseException e) {
            SecurityContextHolder.clearContext();
            restAuthenticationEntryPoint.commence(request, response, new BadCredentialsException("时间戳参数不正确"));
        }
    }

    /**
     * 获取异步处理任务对象
     * @return
     */
    public ASyncSaveApiLogTask getSaveApiLogTask() {
        if(saveApiLogTask == null){
            saveApiLogTask = SpringContextHolder.getApplicationContext().getBean(ASyncSaveApiLogTask.class);
        }
        return saveApiLogTask;
    }

//    /**
//     * 调用日志异步入库
//     * @param appid
//     * @param payload
//     * @param contentType
//     * @param method
//     * @param signature
//     * @param uri
//     */
//    @Async
//    public void invokeApiSaveDB(String appid, String payload, String contentType, String method, String signature, String uri){
////        ApiInvokeLog log = new ApiInvokeLog();
////        log.setAppid(appid);
////        log.setBody(payload);
////        log.setContentType(contentType);
////        log.setMethod(method);
////        log.setSignature(signature);
////        log.setUri(uri);
////        apiInvokeLogService.save(log);
////
////        if(logger.isDebugEnabled()) {
////            logger.debug("调用日志异步入库中完成");
////        }
//    }

}
