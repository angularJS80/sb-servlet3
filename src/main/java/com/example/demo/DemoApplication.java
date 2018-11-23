package com.example.demo;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerPropertiesAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

@ComponentScan(basePackages="com.example.demo")
@SpringBootApplication
@EnableAutoConfiguration
@PropertySources({
	@PropertySource(value="classpath:application.properties")
})
public class DemoApplication extends org.springframework.boot.web.support.SpringBootServletInitializer implements WebApplicationInitializer{

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DemoApplication.class);
    }

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
//		servletContext.addListener(new ContextLoaderListener(context));
		context.setConfigLocation("com.example.demo");
		context.setServletContext(servletContext);

		Dynamic servlet = servletContext.addServlet("DispatcherServlet", new DispatcherServlet(context));
		servlet.setLoadOnStartup(1);
		servlet.addMapping("/");
		servlet.setMultipartConfig(new MultipartConfigElement("", 1024*1024*200, 1024*1024*200, 1024*1024*10));
		
		// CharacterSet Encoding
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);

        FilterRegistration.Dynamic charEncRegi = servletContext.addFilter("characterEncodingFilter", characterEncodingFilter);
        EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD);
        charEncRegi.addMappingForUrlPatterns(dispatcherTypes, true, "/*");

		//FilterRegistration.Dynamic corsFilter = servletContext.addFilter("corsFilter", CORSFilter.class);
        //corsFilter.addMappingForUrlPatterns(null, false, "/*");

		super.onStartup(servletContext);
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}