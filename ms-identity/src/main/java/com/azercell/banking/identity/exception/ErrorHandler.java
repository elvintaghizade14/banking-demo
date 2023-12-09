package com.azercell.banking.identity.exception;

import com.azercell.banking.commonlib.exception.AuthException;
import com.azercell.banking.commonlib.exception.CommonErrorHandler;
import com.azercell.banking.commonlib.exception.model.CommonErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class ErrorHandler extends CommonErrorHandler {

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(AuthException.class)
    public CommonErrorResponse handleAuthException(AuthException ex) {
        addErrorLog(UNAUTHORIZED, ex.getErrorCode(), ex.getMessage(), "AuthException");
        return new CommonErrorResponse(
                webUtil.getRequestId(),
                ex.getErrorCode(),
                ex.getMessage());
    }

}
