package com.azercell.banking.commonlib.exception.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonErrorMessage {

    public static final String REQUIRED_NULL_ERROR_MESSAGE = "{0} must be null";
    public static final String REQUIRED_NON_NULL_ERROR_MESSAGE = "{0} must not be null";
    public static final String USERNAME_INVALID_MESSAGE = "should be in the format example@azercell.com";
    public static final String PASSWORD_INVALID_MESSAGE =                                      // NOSONAR
            "should be including at least 1 uppercase letter, 1 lowercase letter, 1 special character, "
                    + "1 number, and a length between 8 and 30 characters.";
}
