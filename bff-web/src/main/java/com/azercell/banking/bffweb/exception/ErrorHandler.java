package com.azercell.banking.bffweb.exception;

import com.azercell.banking.commonlib.exception.CommonErrorHandler;
import com.azercell.banking.commonlib.exception.constant.CommonErrorCode;
import com.azercell.banking.commonlib.exception.model.CommonErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler extends CommonErrorHandler {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({AccessDeniedException.class})
    public CommonErrorResponse handleAccessDeniedException(AccessDeniedException ex) {
        super.addErrorLog(HttpStatus.FORBIDDEN, CommonErrorCode.FORBIDDEN, ex.getMessage(), "AccessDeniedException");
        return new CommonErrorResponse(super.webUtil.getRequestId(), CommonErrorCode.FORBIDDEN, ex.getMessage());
    }

}
