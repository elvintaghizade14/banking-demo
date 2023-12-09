package com.azercell.banking.commonlib.interceptor;

import com.azercell.banking.commonlib.model.constant.CommonConstants.HttpAttribute;
import com.azercell.banking.commonlib.util.WebUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Log4j2
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "common.interceptor", name = "enabled")
public class CommonRequestInterceptor implements HandlerInterceptor {

    private final WebUtil webUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        log.trace("CommonRequestInterceptor is calling for uri: {} {}", request.getMethod(), request.getRequestURI());

        request.setAttribute(HttpAttribute.REQUEST_START_TIME, System.currentTimeMillis());
        webUtil.getCustomHeaders().forEach((header, value) -> MDC.put(header.toLowerCase(), value));

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object handler, Exception ex) {
        MDC.clear();
    }

}
