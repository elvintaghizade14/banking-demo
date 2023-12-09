package com.azercell.banking.commonlib.config;

import com.azercell.banking.commonlib.interceptor.CommonRequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
@ConditionalOnBean(value = CommonRequestInterceptor.class)
public class InterceptorConfig implements WebMvcConfigurer {

    @Value("${application.interceptor.common-request.exclude-path-patterns:/swagger**/**,/**/api-docs,/error}")
    private String[] excludePaths;

    private final CommonRequestInterceptor commonRequestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonRequestInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePaths);
    }

}
