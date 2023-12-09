package com.azercell.banking.commonlib.exception.validation.constraint;

import com.azercell.banking.commonlib.exception.constant.CommonErrorMessage;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Pattern(
        regexp = "^[\\w.+\\-]+@azercell\\.com$",
        message = CommonErrorMessage.USERNAME_INVALID_MESSAGE
)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
public @interface Username {

    String message() default "should be in the format example@azercell.com";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
