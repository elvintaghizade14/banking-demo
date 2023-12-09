package com.azercell.banking.bffweb.security.filter;

import com.azercell.banking.bffweb.model.CustomUserPrincipal;
import com.azercell.banking.bffweb.model.enums.TokenType;
import com.azercell.banking.bffweb.security.provider.TokenProvider;
import com.azercell.banking.bffweb.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.debug("JwtTokenFilter is calling for uri: {} {}",
                request.getMethod(), request.getRequestURI());

        final String jwt = tokenProvider.resolveToken(request);
        if (StringUtils.hasText(jwt) && tokenProvider.isValidToken(jwt)) {
            Authentication authentication = tokenProvider.parseAuthentication(jwt);
            CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
            if (redisService.read(authentication.getName())
                    && TokenType.ACCESS.name().equals(principal.getTokenType())) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

}
