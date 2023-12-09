package com.azercell.banking.commonlib.exception.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonErrorCode {

    public static final String FORBIDDEN = "forbidden";
    public static final String UNAUTHORIZED = "unauthorized";
    public static final String ALREADY_EXIST = "already_exist";
    public static final String DATA_NOT_FOUND = "data_not_found";
    public static final String RESOURCE_MISSING = "resource_missing";
    public static final String PARAMETER_INVALID = "parameter_invalid";
    public static final String REQUEST_INVALID = "request_body_invalid";
    public static final String UNEXPECTED_INTERNAL_ERROR = "unexpected_internal_error";

}
