package com.azercell.banking.commonlib.util;

import com.azercell.banking.commonlib.model.constant.CommonConstants.HttpAttribute;
import com.azercell.banking.commonlib.model.constant.CommonConstants.HttpHeader;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Log4j2
@Component
public class WebUtil {

    @Autowired
    private Tracer tracer;

    private ContentCachingRequestWrapper requestWrapper;

    public void initializeRequestWrapper(ContentCachingRequestWrapper requestWrapper) {
        this.requestWrapper = requestWrapper;
    }

    public String getClientIp() {
        final HttpServletRequest request = getHttpServletRequest();
        if (request == null) {
            return "";
        }
        return Optional.ofNullable(request.getHeader(HttpHeader.X_FORWARDED_FOR))
                .orElse(request.getRemoteAddr());
    }

    public String getRequestId() {
        return Optional.ofNullable(getHttpServletRequest())
                .map(req -> !ObjectUtils.isEmpty(req.getHeader(HttpHeader.X_REQUEST_ID))
                        ? req.getHeader(HttpHeader.X_REQUEST_ID) : getSleuthTraceId())
                .orElse(null);
    }

    public String getRequestUri() {
        return Optional.ofNullable(getHttpServletRequest())
                .map(req -> req.getMethod() + " " + req.getRequestURI())
                .orElse("");
    }

    public String getRequestUriWithQueryString() {
        return Optional.ofNullable(getHttpServletRequest())
                .map(req -> {
                    final String uri = req.getQueryString() != null
                            ? String.join("?", req.getRequestURI(), req.getQueryString()) :
                            req.getRequestURI();
                    return req.getMethod() + " " + uri;
                })
                .orElse("");
    }

    public Map<String, String> getRequestHeadersInfo() {
        final Map<String, String> map = new HashMap<>();
        final HttpServletRequest request = getHttpServletRequest();
        if (request == null) {
            return map;
        }
        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    public Map<String, String> getCustomHeaders() {
        final Map<String, String> headers = new HashMap<>();
        final HttpServletRequest request = getHttpServletRequest();
        if (request == null) {
            return headers;
        }
        request.getHeaderNames()
                .asIterator()
                .forEachRemaining(headerName -> {
                    if (headerName.toUpperCase().startsWith("BOS")) {
                        headers.put(headerName, request.getHeader(headerName));
                    }
                });
        return headers;
    }

    public Long getElapsedTime() {
        return Optional.ofNullable(getHttpServletRequest())
                .map(req -> String.valueOf(req.getAttribute(HttpAttribute.REQUEST_START_TIME)))
                .filter(StringUtils::isNumeric)
                .map(t -> System.currentTimeMillis() - Long.parseLong(t))
                .orElse(-1L);
    }

    public String getBearerTokenFromAuthorizationHeader() {
        return Optional.ofNullable(getHttpServletRequest())
                .map(request -> request.getHeader(HttpHeader.AUTHORIZATION))
                .filter(token -> token.startsWith(HttpAttribute.BEARER))
                .orElse(null);
    }

    public String getRequestBody() {
        try {
            return Optional.ofNullable(requestWrapper)
                    .map(ContentCachingRequestWrapper::getContentAsByteArray)
                    .map(String::new)
                    .orElse("[null]");
        } catch (Exception e) {
            log.error("{} error occurs while getting request body for uri {}", e.getMessage(), getRequestUri(), e);
            return "[failed]";
        }
    }

    public String getSleuthTraceId() {
        final Span span = tracer.currentSpan();
        return span != null ? span.context().traceId() : "";
    }

    public String getSleuthSpanId() {
        final Span span = tracer.currentSpan();
        return span != null ? span.context().spanId() : "";
    }

    private HttpServletRequest getHttpServletRequest() {
        if (requestWrapper != null && requestWrapper.getRequest() != null) {
            return (HttpServletRequest) requestWrapper.getRequest();
        }
        if (RequestContextHolder.getRequestAttributes() != null) {
            return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        }
        log.warn("HttpServletRequest is null");
        return null;
    }

}
