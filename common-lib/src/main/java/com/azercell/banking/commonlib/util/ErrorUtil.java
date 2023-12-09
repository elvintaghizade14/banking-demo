package com.azercell.banking.commonlib.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.validation.Path;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorUtil {

    public static String getPropertyName(Path propertyPath) {
        String propertyName = null;
        for (Path.Node node : propertyPath) {
            propertyName = node.getName();
        }
        return propertyName != null ? propertyName : propertyPath.toString();
    }

}
