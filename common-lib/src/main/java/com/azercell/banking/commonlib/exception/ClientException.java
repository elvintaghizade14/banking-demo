package com.azercell.banking.commonlib.exception;

import lombok.Getter;

@Getter
public class ClientException extends RuntimeException {

    private final int status;
    private final String requestId;
    private final String errorCode;
    private final String message;

    public ClientException(int status, String requestId, String errorCode, String message) {
        super(errorCode);
        this.status = status;
        this.requestId = requestId;
        this.errorCode = errorCode;
        this.message = message;
    }

}
