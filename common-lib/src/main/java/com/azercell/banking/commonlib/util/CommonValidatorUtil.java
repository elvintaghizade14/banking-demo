package com.azercell.banking.commonlib.util;

import com.azercell.banking.commonlib.exception.InvalidInputException;
import com.azercell.banking.commonlib.exception.constant.CommonErrorCode;
import com.azercell.banking.commonlib.exception.constant.CommonErrorMessage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonValidatorUtil {

    public static <T> void requireNonNull(final T t, final String paramName) {
        if (t == null) {
            throw InvalidInputException.of(CommonErrorCode.REQUEST_INVALID,
                    CommonErrorMessage.REQUIRED_NON_NULL_ERROR_MESSAGE, paramName);
        }
    }

    public static <T> void requireNull(final T t, final String paramName) {
        if (t != null) {
            throw InvalidInputException.of(CommonErrorCode.REQUEST_INVALID,
                    CommonErrorMessage.REQUIRED_NULL_ERROR_MESSAGE, paramName);
        }
    }

}
