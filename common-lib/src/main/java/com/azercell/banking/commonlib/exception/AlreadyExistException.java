package com.azercell.banking.commonlib.exception;

import com.azercell.banking.commonlib.exception.constant.CommonErrorCode;

import java.text.MessageFormat;

public class AlreadyExistException extends CommonException {

    public AlreadyExistException(String message) {
        super(CommonErrorCode.ALREADY_EXIST, message);
    }

    public static AlreadyExistException of(String message, Object... args) {
        return new AlreadyExistException(MessageFormat.format(message, args));
    }

}
