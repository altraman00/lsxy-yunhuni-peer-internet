package com.lsxy.app.portal.base;

import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by liups on 2016/7/7.
 */
@Component
public class PortalApiExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ex.printStackTrace();;
        RestResponse failed = RestResponse.failed("0000", ex.getMessage());
        return null;
    }
}
