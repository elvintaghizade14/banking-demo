package com.azercell.banking.commonlib.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.MessageFormat;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FormatterUtil {

    public static String message(String message, Object... args) {
        return MessageFormat.format(message, args);
    }

}
