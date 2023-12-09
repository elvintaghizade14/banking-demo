package com.azercell.banking.commonlib.exception;

import com.azercell.banking.commonlib.exception.constant.CommonErrorCode;

import java.text.MessageFormat;

public class ForbiddenException extends CommonException {

    public ForbiddenException(String message) {
        super(CommonErrorCode.FORBIDDEN, message);
    }

    public static ForbiddenException of(String message, Object... args) {
        return new ForbiddenException(MessageFormat.format(message, args));
    }

}
