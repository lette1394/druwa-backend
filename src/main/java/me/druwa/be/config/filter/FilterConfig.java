package me.druwa.be.config.filter;

import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static me.druwa.be.log.LoggingUtils.DEFAULT_MDC_UUID_TOKEN_KEY;
import static me.druwa.be.log.LoggingUtils.DEFAULT_RESPONSE_TOKEN_HEADER;

@Configuration
class FilterConfig {
    @Bean
    public FilterRegistrationBean<Filter> debugTokenBean() {
        final FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        final Filter log4jMDCFilterFilter = new DebugTokenInjectFilter(DEFAULT_RESPONSE_TOKEN_HEADER,
                                                                       DEFAULT_MDC_UUID_TOKEN_KEY);

        registrationBean.setFilter(log4jMDCFilterFilter);
        registrationBean.setOrder(-500);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> accessLogBean() {
        final FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        final Filter accessLogFilter = new AccessLogFilter();

        registrationBean.setFilter(accessLogFilter);
        registrationBean.setOrder(-499);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> exceptionLogBean() {
        final FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        final Filter accessLogFilter = new ExceptionFilter();

        registrationBean.setFilter(accessLogFilter);
        registrationBean.setOrder(-498);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> httpMessageLogBean() {
        final FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        final Filter accessLogFilter = new HttpMessageJsonLogFilter();

        registrationBean.setFilter(accessLogFilter);
        registrationBean.setOrder(-497);
        return registrationBean;
    }
}

