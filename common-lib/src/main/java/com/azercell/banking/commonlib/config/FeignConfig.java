package com.azercell.banking.commonlib.config;

import com.azercell.banking.commonlib.util.WebUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import feign.okhttp.OkHttpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableFeignClients(basePackages = "com.azercell.banking")
@ConditionalOnProperty(prefix = "common.openfeign", name = "enabled")
public class FeignConfig {

    private final WebUtil webUtil;
    private final ObjectMapper objectMapper;

    @Bean
    public ErrorDecoder errorDecoder() {
        return new ClientErrorDecoder(objectMapper);
    }

    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }

    @Bean
    public RequestInterceptor feignRequestInterceptor() {
        return requestTemplate -> webUtil.getCustomHeaders().forEach(requestTemplate::header);
    }

}
