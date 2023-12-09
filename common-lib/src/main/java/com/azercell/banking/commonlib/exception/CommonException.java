package com.azercell.banking.commonlib.exception;

import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {

    private final String errorCode;
    private final String message;

    public CommonException(String errorCode, String message) {
        super(errorCode);
        this.errorCode = errorCode;
        this.message = message;
    }

}
