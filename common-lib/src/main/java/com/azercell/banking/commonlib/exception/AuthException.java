package com.azercell.banking.commonlib.exception;

import com.azercell.banking.commonlib.exception.constant.CommonErrorCode;

import java.text.MessageFormat;

public class AuthException extends CommonException {

    public AuthException(String message) {
        super(CommonErrorCode.UNAUTHORIZED, message);
    }

    public static AuthException of(String message, Object... args) {
        return new AuthException(MessageFormat.format(message, args));
    }

}
