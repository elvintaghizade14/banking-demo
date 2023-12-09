package com.azercell.banking.commonlib.filter;

import com.azercell.banking.commonlib.util.WebUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Log4j2
@Component
@RequiredArgsConstructor
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@WebFilter(filterName = "ContentCachingFilter", urlPatterns = "/*")
@ConditionalOnProperty(prefix = "common.content-caching", name = "enabled")
public class ContentCachingFilter extends OncePerRequestFilter {

    @Value("${common.content-caching.include-paths:}")
    private String[] includePaths;
    private final WebUtil webUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.trace("ContentCachingFilter is calling for uri: {} {}", request.getMethod(), request.getRequestURI());

        final ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        wrappedRequest.getParameterMap();
        webUtil.initializeRequestWrapper(wrappedRequest);

        filterChain.doFilter(wrappedRequest, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !HttpMethod.POST.matches(request.getMethod()) && !matchRequestUri(request);
    }

    private boolean matchRequestUri(HttpServletRequest request) {
        return Arrays.stream(includePaths)
                .anyMatch(path -> new AntPathMatcher().match(path, request.getRequestURI()));
    }

}
