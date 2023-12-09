package com.azercell.banking.commonlib.exception;

import com.azercell.banking.commonlib.exception.constant.CommonErrorCode;

import java.text.MessageFormat;

public class DataNotFoundException extends CommonException {

    public DataNotFoundException(String message) {
        super(CommonErrorCode.DATA_NOT_FOUND, message);
    }

    public static DataNotFoundException of(String message, Object... args) {
        return new DataNotFoundException(MessageFormat.format(message, args));
    }

}
