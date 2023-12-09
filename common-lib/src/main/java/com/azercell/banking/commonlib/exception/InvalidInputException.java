package com.azercell.banking.commonlib.exception;

import com.azercell.banking.commonlib.exception.validation.MessageValidator;

import java.text.MessageFormat;

public class InvalidInputException extends CommonException {

    public InvalidInputException(String errorCode, String message) {
        super(errorCode, message);
    }

    public static InvalidInputException of(String errorCode, String message, Object... args) {
        return new InvalidInputException(errorCode, MessageFormat.format(message, args));
    }

    public static InvalidInputException of(MessageValidator validator) {
        return new InvalidInputException(validator.code(), validator.message());
    }

}
