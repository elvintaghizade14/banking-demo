package com.azercell.banking.bffweb.security.handler;

import com.azercell.banking.bffweb.exception.constant.ErrorMessage;
import com.azercell.banking.commonlib.exception.constant.CommonErrorCode;
import com.azercell.banking.commonlib.exception.model.CommonErrorResponse;
import com.azercell.banking.commonlib.util.WebUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import net.logstash.logback.argument.StructuredArguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Log4j2
@Component
public class SecurityProblemHandler implements AuthenticationEntryPoint {

    @Autowired
    private WebUtil webUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public void commence(final HttpServletRequest request, final HttpServletResponse response,
                         final AuthenticationException exception) {
        log.error("[Error] | Status: {} | Code: {} | Type: {} | Path: {} | Elapsed time: {} ms | Message: {}",
                HttpStatus.UNAUTHORIZED, CommonErrorCode.UNAUTHORIZED, "AuthenticationException",
                webUtil.getRequestUri(), StructuredArguments.v("elapsed_time", webUtil.getElapsedTime()),
                ErrorMessage.INVALID_TOKEN_MESSAGE);

        final CommonErrorResponse errorResponse = new CommonErrorResponse(webUtil.getRequestId(),
                CommonErrorCode.UNAUTHORIZED, ErrorMessage.INVALID_TOKEN_MESSAGE);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        response.getWriter().flush();
        response.getWriter().close();
    }

}
