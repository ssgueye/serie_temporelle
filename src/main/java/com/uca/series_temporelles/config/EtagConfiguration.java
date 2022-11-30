package com.uca.series_temporelles.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@Configuration
public class EtagConfiguration {

    @Bean
    public FilterRegistrationBean<ShallowEtagHeaderFilter> shallowEtagHeaderFilter(){
        FilterRegistrationBean<ShallowEtagHeaderFilter> filterRegistrationBean = new FilterRegistrationBean(new ShallowEtagHeaderFilter());
        filterRegistrationBean.addUrlPatterns("/api/series/*", "/api/events/*", "/api/user_series/*");
        filterRegistrationBean.setName("etagFilter");

        return filterRegistrationBean;
    }
}
