package com.lsxy.app.portal;

import com.lsxy.app.portal.config.SpringStartupConfig;
import com.lsxy.framework.core.web.SpringContextUtil;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by Tandy on 2016/6/6.
 *
 * UPDATE: Note that above class can be written even more concisely [and it's the preferred way], by extending AbstractAnnotationConfigDispatcherServletInitializer base class, as shown below:

 package com.websystique.springmvc.configuration;

 import org.springframework.comm.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

 public class HelloWorldInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

@Override
protected Class<?>[] getRootConfigClasses() {
return new Class[] { HelloWorldConfiguration.class };
}

@Override
protected Class<?>[] getServletConfigClasses() {
return null;
}

@Override
protected String[] getServletMappings() {
return new String[] { "/" };
}

}
 */
public class SpringInitializer implements WebApplicationInitializer {

    public static final String systemId = "app.portal";
    static {
        System.setProperty("systemId",systemId);
    }

    @Override
    public void onStartup(ServletContext container) throws ServletException {


        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(SpringStartupConfig.class);


        ctx.setServletContext(container);
        //由于和spring security 结合，需要将该过滤器添加到security filter中
//        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
//        characterEncodingFilter.setEncoding("UTF-8");
//
//        container.addFilter("characterEncodingFilter",characterEncodingFilter).addMappingForUrlPatterns(null,false,"/*");

        ServletRegistration.Dynamic servlet = container.addServlet("dispatcher", new DispatcherServlet(ctx));

        servlet.setLoadOnStartup(1);
        servlet.setAsyncSupported(true);
        servlet.addMapping("/");
        SpringContextUtil.setApplicationContext(ctx);
    }
}
