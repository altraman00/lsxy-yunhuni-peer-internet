package com.lsxy.app.portal.aop;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 表单重复提交的拦截器.
 * 
 * @author WangYun
 *
 */
@Aspect
public class DuplicateSubmitInterceptor extends HandlerInterceptorAdapter {
	/**
	 * 
	 */
	private Log logger = LogFactory.getLog(DuplicateSubmitInterceptor.class);

	/**
	 * 
	 */
	@Override
    public final boolean preHandle(final HttpServletRequest request, 
    		final HttpServletResponse response, final Object handler) 
    				throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
	 
			AvoidDuplicateSubmit annotation = 
					method.getAnnotation(AvoidDuplicateSubmit.class);
	        if (annotation != null) {
	            boolean needValidate = annotation.needValidate();
	            if (needValidate) {
	                if (isRepeatSubmit(request)) {
	                	logger.debug("检测到重复提交表单:" + request.getRequestURI());
			            return false;
			        }
	                request.getSession(false).removeAttribute("token");
	            }
	        }
		}
		return true;
	}
 
	/**
	 * 
	 * @param request
	 *    sss
	 * @return 
	 *    sss
	 * 
	 */
	private boolean isRepeatSubmit(final HttpServletRequest request) {
        String serverToken = (String) 
        		request.getSession(false).getAttribute("token");
        logger.debug("server token:" + serverToken);
        if (serverToken == null) {
            return true;
        }
        String clinetToken = request.getParameter("token");
        logger.debug("client token:" + serverToken);
        if (clinetToken == null) {
            return true;
        }
        if (!serverToken.equals(clinetToken)) {
            return true;
        }
        return false;
    }
}
