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
        regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$+%^&*-]).{8,}$",   // NOSONAR
        message = CommonErrorMessage.PASSWORD_INVALID_MESSAGE
)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
public @interface Password {

    String message() default
            "should be including at least 1 uppercase letter, 1 lowercase letter, 1 special character, "
                    + "1 number, and a length between 8 and 30 characters.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
