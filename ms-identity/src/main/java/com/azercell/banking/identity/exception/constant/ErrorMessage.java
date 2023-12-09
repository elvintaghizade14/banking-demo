package com.azercell.banking.identity.exception.constant;

import com.azercell.banking.commonlib.exception.constant.CommonErrorMessage;

public final class ErrorMessage extends CommonErrorMessage {

    public static final String USER_ALREADY_EXIST_MESSAGE = "User with username - {0} already exists!";
    public static final String USER_NOT_FOUND_MESSAGE = "User with {0} - {1} not found!";
    public static final String USER_IS_NOT_ACTIVE_MESSAGE = "User with username - {0} is {1}!";
    public static final String UNAUTHORIZED_MESSAGE = "Username or password is incorrect!";
    public static final String INVALID_TOKEN_MESSAGE = "Invalid token!";

}
