package com.everseeker.web;

import com.everseeker.config.RootConfig;
import com.everseeker.config.WebConfig;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Created by everseeker on 16/9/8.
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected String[] getServletMappings() {       //将DispatcherServlet映射到"/"
        return new String[] { "/" };
    }

    /**
     * RootConfig类用来配置ContextLoaderListener创建的应用上下文中的bean,
     * 比如@Repository, @Service等组件
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] { RootConfig.class };
    }

    /**
     * DispatcherServlet加载应用上下文时,使用定义在WebConfig配置类中的bean,
     * 用来加载包含Web组件的bean,比如控制器,视图解析器以及处理器映射,  @Controller, @RequestMapping等
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { WebConfig.class };
    }

}
