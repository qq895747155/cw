package com.cw.chwo.springconfig;

import com.cw.chwo.common.annotationmerge.WiselyConfiguration;
import com.cw.chwo.common.interceptor.MyInterceptor;
import com.cw.chwo.module.User;
import com.cw.chwo.springconfig.viewresolver.Jaxb2MarshallingXmlViewResolver;
import com.cw.chwo.springconfig.viewresolver.JsonViewResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafView;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by handl on 2017/5/21.
 *
 *    ChwoWebSpringMvcConfig：SpringWeb容器环境的配置
 *          艾特Configuration：定义此文件为spring的java配置文件
 *          艾特EnableWebMvc：启用SpringMVC
 *          艾特ComponentScan：扫描controller包，通常这是controller组件的存放目录
 *          艾特Bean：配置一个bean(相当于在xml中配置一个<bean></bean>),会加入到spring容日中
 *          viewResolver():配置web试图解析器
 *          继承WebMvcConfigurerAdapter并覆盖 configureDefaultServletHandling： 要求DispatcherServlet 将对静态资源的请求转发到 Servlet 容器
                中默认的 Servlet 上，而不是使用 DispatcherServlet 本身来处理此类请求
 *          继承WebMvcConfigurerAdapter并覆盖configureContentNegotiation(ContentNegotiationConfigurer configurer): 设置多类型试图的默认方式，
 *          contentNegotiatingViewResolver(ContentNegotiationManager manager)：contentNegotiatingViewResolver添加ContentNegotiationManager
 *          jaxb2MarshallingXmlViewResolver()：配置xml视图解析类型
 *          jsonViewResolver()：配置json视图解析类型
 *
 *
 *
 *          注意：配置configurePathMatch方法后，会导致数据试图失效(.xml/.json)
 *          注意：jsp视图和thymeleaf视图在匹配不成功时也会返回true，
 *                  因此可以通过设置ViewResolver.setViewNames()方法来解决冲突问题
 */
@EnableWebMvc
@WiselyConfiguration(basePackages = "com.cw.chwo.controller")
public class WebSpringMvcConfig extends WebMvcConfigurerAdapter{

    @Autowired
    private MyInterceptor myInterceptor;

    /**
     * 配置ContentNegotiatingViewResolver默认的处理类型，本例为html
     * @param configurer
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        //ignoreAcceptHeader 排除请求头
        configurer.ignoreAcceptHeader(true).defaultContentType(MediaType.TEXT_HTML);
    }

    /**
     * 配置ContentNegotiatingViewResolver
     * 多视图解析器(json,xml...)，默认以后缀方式
     * @param manager
     * @return
     */
    @Bean
    public ContentNegotiatingViewResolver contentNegotiatingViewResolver(ContentNegotiationManager manager){
        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        resolver.setOrder(1);
        resolver.setContentNegotiationManager(manager);

        List<ViewResolver> list = new ArrayList<>();
        list.add(jaxb2MarshallingXmlViewResolver());
        list.add(jsonViewResolver());
        resolver.setViewResolvers(list);
        return resolver;
    }

    /**
     * 配置xml解析类型
     * @return
     */
    @Bean
    public ViewResolver jaxb2MarshallingXmlViewResolver() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(User.class);
        marshaller.setCheckForXmlRootElement(true);
        return new Jaxb2MarshallingXmlViewResolver(marshaller);
    }

    /**
     * 配置json解析类型
     * @return
     */
    @Bean
    public ViewResolver jsonViewResolver() {
        return new JsonViewResolver();
    }


    /**************************     velocity视图配置开始    *********************************/
    /**
     * jsp视图配置(内部资源视图)
     * @return
     */
    @Bean
    public InternalResourceViewResolver jspViewResolver(){
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        //设置优先级
        resolver.setOrder(9);
//        resolver.setPrefix("/WEB-INF/view/userhome/");

        resolver.setPrefix("/WEB-INF/view/");
        resolver.setViewNames("userhome*");

        resolver.setSuffix(".jsp");
        //配置可以解释jstl视图的class
        resolver.setViewClass(org.springframework.web.servlet.view.JstlView.class);
        resolver.setExposeContextBeansAsAttributes(true);
        return resolver;
    }
    /**************************     jsp视图配置完成    *********************************/


    /**************************     velocity视图配置开始    *********************************/
    /**
     * velocity配置，设置模板属性
     * @return
     */
    @Bean
    public VelocityConfigurer velocityConfigurer(){
        VelocityConfigurer velocityConfigurer = new VelocityConfigurer();
//        velocityConfigurer.setResourceLoaderPath("/WEB-INF/view/velocity/");
        velocityConfigurer.setResourceLoaderPath("/WEB-INF/view/");
        velocityConfigurer.setConfigLocation(new ClassPathResource("velocity.properties"));
        return velocityConfigurer;
    }

    /**
     * velocity视图,视图配置
     * @return
     */
    @Bean
    public VelocityViewResolver velocityViewResolver(){
        VelocityViewResolver viewResolver = new VelocityViewResolver();
        viewResolver.setOrder(7);
        viewResolver.setSuffix(".vm");
        viewResolver.setViewNames("velocity*");
        viewResolver.setContentType("text/html;charset=UTF-8");
        return viewResolver;
    }
    /**************************     velocity视图配置完成    *********************************/

    /**************************     ThymeLeaf视图配置开始    *********************************/
    /**
     * SpringResourceTemplateResolver ，设置模板属性
     * @return
     */
    @Bean
    public ITemplateResolver templateResolver(){
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
//        templateResolver.setPrefix("/WEB-INF/view/thymeleaf/");

        templateResolver.setPrefix("/WEB-INF/view/");

        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCacheable(false);
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    /**
     * SpringTemplateEngine：用来驱动在springmvc下使用thymeleaf模板
     * @return
     */
    @Bean
    public SpringTemplateEngine templateEngine(){
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        return templateEngine;
    }

    /**
     * ThymeleafViewResolver：设置thymeleaf视图属性
     * @return
     */
    @Bean
    public ThymeleafViewResolver viewResolver(){
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");
        viewResolver.setViewClass(ThymeleafView.class); //默认设置
        viewResolver.setOrder(8);
        viewResolver.setViewNames(new String[]{"thymeleaf*"});
        return viewResolver;
    }


    /**************************     ThymeLeaf视图配置完成    *********************************/


    /**
     * 静态资源处理
     * @param configurer
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /**
     * 静态文件访问
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/js/**").addResourceLocations("/WEB-INF/js/");
    }

    /**
     * 页面转向，单纯的跳后台转换页面(无任何业务处理)可以通过这种方式处理
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/toUpload").setViewName("uploadfile");
        registry.addViewController("/sse").setViewName("sse");
        registry.addViewController("/servletasync").setViewName("servletasync");
    }

    /**
     * 路径参数如果带‘.’的话，‘.’后面的值将被忽略掉，
     * 通过重写configurePathMatch方法可不忽略‘.’后面的值
     * @param configurer
     *
     * 注意注意注意注意注意注意：配置configurePathMatch方法后，会导致数据试图失效(.xml/.json)
     * 注意注意注意注意注意注意：配置configurePathMatch方法后，会导致数据试图失效(.xml/.json)
     * 注意注意注意注意注意注意：配置configurePathMatch方法后，会导致数据试图失效(.xml/.json)
     */
    /*@Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false);
    }*/

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor);
    }
}
