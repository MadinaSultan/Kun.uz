//package com.example.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class SecuredFilterConfig {
//
//    @Autowired
//    private TokenFilter tokenFilter;
//
//    @Bean
//    public FilterRegistrationBean filterRegistrationBean() {
//        FilterRegistrationBean bean = new FilterRegistrationBean();
//        bean.setFilter(tokenFilter);
////        bean.addUrlPatterns("/profile/*");
////        bean.addUrlPatterns("/profile/filter");
//        bean.addUrlPatterns("/region/admin/*");
//        bean.addUrlPatterns("/articleType/admin/*");
//        bean.addUrlPatterns("/category/admin/*");
//        bean.addUrlPatterns("/attach/pagination");
//        bean.addUrlPatterns("/attach/delete");
//        bean.addUrlPatterns("/article/admin");
//        bean.addUrlPatterns("/article/update");
//        bean.addUrlPatterns("/article/delete");
//        bean.addUrlPatterns("/article/status");
//        bean.addUrlPatterns("/category/*");
//        bean.addUrlPatterns("/comment/create");
//        bean.addUrlPatterns("/comment/update");
//        bean.addUrlPatterns("/comment/pageComment");
//
//        return bean;
//    }
//}
