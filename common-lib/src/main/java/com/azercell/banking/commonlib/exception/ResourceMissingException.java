package com.azercell.banking.commonlib.exception;

import com.azercell.banking.commonlib.exception.constant.CommonErrorCode;

import java.text.MessageFormat;

public class ResourceMissingException extends CommonException {

    public ResourceMissingException(String message) {
        super(CommonErrorCode.RESOURCE_MISSING, message);
    }

    public static ResourceMissingException of(String message, Object... args) {
        return new ResourceMissingException(MessageFormat.format(message, args));
    }

}
