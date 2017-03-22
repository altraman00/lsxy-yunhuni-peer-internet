package com.lsxy.app.api.gateway.security.auth;

import com.lsxy.app.api.gateway.util.Constants;
import com.lsxy.app.api.gateway.util.SpringContextHolder;
import com.lsxy.framework.core.utils.StringUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
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
 * 此处一定不能继承GenericFilterBean,一定要继承OncePerRequestFilter
 * 由于使用了异步请求,普通的过滤器会被多次触发
 */
//@Component  不能打开该注释，否则会出问题
public class SignatureAuthFilter extends OncePerRequestFilter{


    private AuthenticationManager authenticationManager;

    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    private ASyncSaveApiLogTask saveApiLogTask;

    private RequestMatcher requestMatcher;

    private static final Logger logger = LoggerFactory.getLogger(SignatureAuthFilter.class);
    // Enable Multi-Read for PUT and POST requests
    private static final Set<String> METHOD_HAS_CONTENT = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER) {
        private static final long serialVersionUID = 1L;
        { add("PUT"); add("POST"); }
    };


    public SignatureAuthFilter(AuthenticationManager authenticationManager , RequestMatcher requestMatcher){
        this.authenticationManager = authenticationManager;
        this.restAuthenticationEntryPoint = new RestAuthenticationEntryPoint();
        this.requestMatcher = requestMatcher;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws ServletException, IOException {

        if(!requestMatcher.matches(req)){
            chain.doFilter(req,resp);
            return;
        }
        //TODO 测试环境暗码凭证生成
        if((req.getHeader("MASKCODE")!=null && "kj38kghl6d93kgj8".equals(req.getHeader("MASKCODE")))){
            RestToken codeToken = new RestToken("MASKCODE", null, new Date(), null,null, null);
            //认证成功，设置认证token
            SecurityContextHolder.getContext().setAuthentication(codeToken);
            chain.doFilter(req, resp);
            return;
        }

        long start = System.currentTimeMillis();

        // use wrapper to read multiple times the content
        AuthenticationRequestWrapper request = new AuthenticationRequestWrapper((HttpServletRequest) req);
        HttpServletResponse response = (HttpServletResponse) resp;

        if (logger.isDebugEnabled()) {
            logger.debug("进入签名认证过滤器：" + request.getRequestURI());
        }
        //参数签名
        String signature = request.getHeader("Signature");
        //鉴权账号
        String certID = request.getHeader("CertID");
        String timestamp = request.getHeader("Timestamp");
        String appid = request.getHeader("AppID");
        String payload = request.getPayload();
        String method = request.getMethod();
        try {
            // If there's not credentials return...
            if (StringUtil.isEmpty(certID) ||
                    StringUtil.isEmpty(signature) ||
                    StringUtil.isEmpty(timestamp) ||
                    StringUtil.isEmpty(appid)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("没有签名认证凭证：" + request.getRequestURI());
                }
                throw new AuthenticationCredentialsNotFoundException("没有找到授权凭证");
            }
            if (logger.isDebugEnabled()) {
                logger.debug("CertID:" + certID+";");
                logger.debug("Timestamp:" + timestamp+";");
                logger.debug("AppID:" + appid+";");
            }

            // Authorization header is in the form <public_access_key>:<signature>
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

            Date date = DateUtils.parseDate(timestamp, "yyyyMMddHHmmss");


            // Create an authentication token
            Authentication authentication = new RestToken(certID, restCredential, date,appid);

            Authentication successfulAuthentication = authenticationManager.authenticate(authentication);

            //认证成功，设置认证token
            SecurityContextHolder.getContext().setAuthentication(successfulAuthentication);

            //调用日志异步入库
            String tenantId = null;
            if(successfulAuthentication instanceof RestToken){
                RestToken restToken = (RestToken) successfulAuthentication;
                tenantId = restToken.getTenantId();
                if(restToken.getSubaccountId()!=null){
                    request.setAttribute(Constants.SUBACCOUNT,restToken.getSubaccountId());
                }
            }
            getSaveApiLogTask().invokeApiSaveDB(req.getRequestURI(),req.getMethod(),appid, payload, contentType, signature,tenantId,certID);

            if(logger.isDebugEnabled()){
                logger.debug("签名校验完毕,花费{}ms",(System.currentTimeMillis()-start));
            }

            start = System.currentTimeMillis();
            // Continue with the Filters
            chain.doFilter(request, response);
            if(logger.isDebugEnabled()){
                logger.debug("API调用完成：{}", apiuri);
                logger.debug("执行体执行完毕,花费:{}ms",(System.currentTimeMillis() - start));
            }

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
