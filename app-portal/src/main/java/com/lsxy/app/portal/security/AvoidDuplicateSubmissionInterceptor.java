package com.lsxy.app.portal.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * <p>
 * 防止重复提交拦截器
 * </p>
 * Created by liups on 2016/6/24.
 */

public class AvoidDuplicateSubmissionInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(AvoidDuplicateSubmissionInterceptor.class);
    private static String SUBMISSION_TOKEN = "submission_token";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            AvoidDuplicateSubmission annotation = method.getAnnotation(AvoidDuplicateSubmission.class);
            if (annotation != null) {
                //为了应对执行的方法同时有检验和生成token的功能，应该先检验，再生成token
                boolean needRemoveSession = annotation.needRemoveToken();
                if (needRemoveSession) {
                    if (isRepeatSubmit(request)) {
                        if(logger.isDebugEnabled()){
                            logger.debug("检验重复提交token失败！");
                        }
                        return false;
                    }
                    request.getSession().removeAttribute(SUBMISSION_TOKEN);
                }

                boolean needSaveSession = annotation.needSaveToken();
                if (needSaveSession) {
                    request.getSession().setAttribute(SUBMISSION_TOKEN, UUID.randomUUID().toString());
                }
            }
            return true;
        }else{
            return super.preHandle(request, response, handler);
        }
    }

    private boolean isRepeatSubmit(HttpServletRequest request) {
        String serverToken = (String) request.getSession().getAttribute(SUBMISSION_TOKEN);
        if (serverToken == null) {
            return true;
        }
        String clientToken = request.getParameter(SUBMISSION_TOKEN);
        if (clientToken == null) {
            return true;
        }
        if (!serverToken.equals(clientToken)) {
            return true;
        }
        return false;
    }
}
