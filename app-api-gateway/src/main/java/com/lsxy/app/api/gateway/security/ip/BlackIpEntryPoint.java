package com.lsxy.app.api.gateway.security.ip;

import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Created by Tandy on 2016/7/1.
 */
public class BlackIpEntryPoint extends Http403ForbiddenEntryPoint {


//    private static final Log logger = LogFactory.getLog(Http403ForbiddenEntryPoint.class);
//
//    /**
//     * Always returns a 403 error code to the client.
//     */
//    public void commence(HttpServletRequest request, HttpServletResponse response,
//                         AuthenticationException arg2) throws IOException, ServletException {
//        if (logger.isDebugEnabled()) {
//            logger.debug("Pre-authenticated entry point called. Rejecting access");
//        }
//        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
//    }

}