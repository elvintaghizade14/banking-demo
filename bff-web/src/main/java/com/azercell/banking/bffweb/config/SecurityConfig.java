package com.azercell.banking.bffweb.config;

import com.azercell.banking.bffweb.security.handler.SecurityProblemHandler;
import com.azercell.banking.bffweb.security.provider.TokenProvider;
import com.azercell.banking.bffweb.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final RedisService redisService;
    private final SecurityProblemHandler securityProblemHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/**/bff-web/**/login")
                .antMatchers("/**/bff-web/**/refresh")
                .antMatchers("/actuator/**")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/swagger-ui/**")
                .antMatchers("/v2/api-docs/**")
                .antMatchers("/v3/api-docs/**")
                .antMatchers("/webjars/**")
                .antMatchers("/favicon.ico")
                .antMatchers("/error");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable();    // NOSONAR

        httpSecurity
                .headers()
                .contentSecurityPolicy("script-src 'self'");

        httpSecurity
                .exceptionHandling()
                .authenticationEntryPoint(securityProblemHandler)

                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/**/login").permitAll()
                .antMatchers("/**/refresh").permitAll()
                .antMatchers(HttpMethod.POST, "/**/users").permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(securityConfigurerAdapter());
        return httpSecurity.build();
    }

    private JwtConfig securityConfigurerAdapter() {
        return new JwtConfig(tokenProvider, redisService);
    }

}
