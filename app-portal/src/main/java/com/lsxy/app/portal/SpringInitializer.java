package com.lsxy.app.portal;

import com.lsxy.app.portal.config.SpringStartupConfig;
import com.lsxy.framework.core.web.SpringContextUtil;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import java.util.EnumSet;

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
public class SpringInitializer extends AbstractHttpSessionApplicationInitializer{

    private final Class<?>[] configurationClasses;


    public SpringInitializer(){
        super(SpringStartupConfig.class);
        configurationClasses = new Class[]{SpringStartupConfig.class};
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        this.beforeSessionRepositoryFilter(servletContext);
        if(this.configurationClasses != null) {
            AnnotationConfigWebApplicationContext rootAppContext = new AnnotationConfigWebApplicationContext();
            rootAppContext.setServletContext(servletContext);
            rootAppContext.register(this.configurationClasses);
            servletContext.addListener(new ContextLoaderListener(rootAppContext));

            ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(rootAppContext));
            servlet.setLoadOnStartup(1);
            servlet.addMapping("/");

            SpringContextUtil.setApplicationContext(rootAppContext);
        }

        this.insertSessionRepositoryFilter(servletContext);
        this.afterSessionRepositoryFilter(servletContext);

    }


    private String getWebApplicationContextAttribute() {
        String dispatcherServletName = this.getDispatcherWebApplicationContextSuffix();
        return dispatcherServletName == null?null:"org.springframework.web.servlet.FrameworkServlet.CONTEXT." + dispatcherServletName;
    }

    private void insertSessionRepositoryFilter(ServletContext servletContext) {
        String filterName = "springSessionRepositoryFilter";
        DelegatingFilterProxy springSessionRepositoryFilter = new DelegatingFilterProxy(filterName);
        String contextAttribute = this.getWebApplicationContextAttribute();
        if(contextAttribute != null) {
            springSessionRepositoryFilter.setContextAttribute(contextAttribute);
        }

        this.registerFilter(servletContext, false, filterName, springSessionRepositoryFilter);
    }

//    private void registerFilters(ServletContext servletContext, boolean insertBeforeOtherFilters, Filter... filters) {
//        Assert.notEmpty(filters, "filters cannot be null or empty");
//        Filter[] var4 = filters;
//        int var5 = filters.length;
//
//        for(int var6 = 0; var6 < var5; ++var6) {
//            Filter filter = var4[var6];
//            if(filter == null) {
//                throw new IllegalArgumentException("filters cannot contain null values. Got " + Arrays.asList(filters));
//            }
//
//            String filterName = Conventions.getVariableName(filter);
//            this.registerFilter(servletContext, insertBeforeOtherFilters, filterName, filter);
//        }
//
//    }


    @Override
    protected String getDispatcherWebApplicationContextSuffix() {
        return "dispatcher";
    }

    private void registerFilter(ServletContext servletContext, boolean insertBeforeOtherFilters, String filterName, Filter filter) {
        FilterRegistration.Dynamic registration = servletContext.addFilter(filterName, filter);
        if(registration == null) {
            throw new IllegalStateException("Duplicate Filter registration for \'" + filterName + "\'. Check to ensure the Filter is only configured once.");
        } else {
            registration.setAsyncSupported(this.isAsyncSessionSupported());
            EnumSet dispatcherTypes = this.getSessionDispatcherTypes();
            registration.addMappingForUrlPatterns(dispatcherTypes, !insertBeforeOtherFilters, new String[]{"/*"});
        }
    }


    //
//    @Override
//    public void onStartup(ServletContext servletContext) throws ServletException {
////            super.onStartup(container);
////        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
////        ctx.register(SpringStartupConfig.class);
////
//////        AnnotationConfigWebApplicationContext ctx = (AnnotationConfigWebApplicationContext) ContextLoader.getCurrentWebApplicationContext();
////
////        ctx.setServletContext(container);
////        //由于和spring security 结合，需要将该过滤器添加到security filter中
//////        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
//////        characterEncodingFilter.setEncoding("UTF-8");
//////
//////        container.addFilter("characterEncodingFilter",characterEncodingFilter).addMappingForUrlPatterns(null,false,"/*");
////
////        ServletRegistration.Dynamic servlet = container.addServlet("dispatcher", new DispatcherServlet(ctx));
////
////        servlet.setLoadOnStartup(1);
////        servlet.addMapping("/");
////
////        SpringContextUtil.setApplicationContext(ctx);
//
//        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
//
//        FilterRegistration.Dynamic securityFilter = servletContext.addFilter("securityFilter", new DelegatingFilterProxy("springSecurityFilterChain"));
//        securityFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
//
//        FilterRegistration.Dynamic characterEncodingFilter = servletContext.addFilter("characterEncodingFilter", new CharacterEncodingFilter());
//        characterEncodingFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
//        characterEncodingFilter.setInitParameter("encoding", "UTF-8");
//        characterEncodingFilter.setInitParameter("forceEncoding", "true");
//
//        servletContext.addListener(new ContextLoaderListener(context));
//        servletContext.setInitParameter("defaultHtmlEscape", "true");
//
//        DispatcherServlet servlet = new DispatcherServlet();
//        // no explicit configuration reference here: everything is configured in the root container for simplicity
//        servlet.setContextConfigLocation("");
//
//        ServletRegistration.Dynamic appServlet = servletContext.addServlet("appServlet", servlet);
//        appServlet.setLoadOnStartup(1);
//        appServlet.setAsyncSupported(true);
//
//        Set<String> mappingConflicts = appServlet.addMapping("/");
//        if (!mappingConflicts.isEmpty()) {
//            throw new IllegalStateException("'appServlet' cannot be mapped to '/' under Tomcat versions <= 7.0.14");
//        }
//        appServlet.setMultipartConfig(new MultipartConfigElement("/tmp", 1024*1024*50, 1024*1024*50*5, 1024*1024));
//
////        //ADDED WITH SPRING-SESSION
////        appendFilters(servletContext, new UserAccountsFilter());
//
//    }
}
