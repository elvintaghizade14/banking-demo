package com.azercell.banking.bffweb.config;

import com.azercell.banking.bffweb.model.CustomUserPrincipal;
import com.azercell.banking.commonlib.config.ClientErrorDecoder;
import com.azercell.banking.commonlib.model.constant.CommonConstants;
import com.azercell.banking.commonlib.util.WebUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.ErrorDecoder;
import feign.okhttp.OkHttpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@RequiredArgsConstructor
@EnableFeignClients(basePackages = "com.azercell.banking.bffweb.client")
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
        return (RequestTemplate requestTemplate) -> {
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof CustomUserPrincipal) {
                final CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
                requestTemplate.header(CommonConstants.HttpHeader.X_USERNAME, principal.getUsername());
                requestTemplate.header(CommonConstants.HttpHeader.X_USER_ID, String.valueOf(principal.getId()));
                requestTemplate.header(CommonConstants.HttpHeader.X_USER_ROLE, principal.getUserRole().name());
                requestTemplate.header(CommonConstants.HttpHeader.X_USER_STATUS, principal.getUserStatus().name());
                webUtil.getCustomHeaders().forEach(requestTemplate::header);
            }
        };
    }

}
