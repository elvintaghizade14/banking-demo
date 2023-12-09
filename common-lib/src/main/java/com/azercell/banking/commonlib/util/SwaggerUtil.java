package com.azercell.banking.commonlib.util;

import com.azercell.banking.commonlib.config.properties.SwaggerProperties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;

import java.util.Collections;
import java.util.function.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SwaggerUtil {

    private static final String ACTUATOR_BASE_PACKAGE = "org.springframework.boot.actuate.endpoint.web";

    public static Predicate<RequestHandler> apis(SwaggerProperties props) {
        Predicate<RequestHandler> apis = RequestHandlerSelectors.basePackage(props.getBasePackage());
        if (props.isEnabledActuatorEndpoints()) {
            apis = apis.or(RequestHandlerSelectors.basePackage(ACTUATOR_BASE_PACKAGE));
        }
        return apis;
    }

    public static ApiInfo convertToSpringFoxApiInfo(SwaggerProperties props) {
        return new ApiInfo(
                props.getTitle(),
                props.getDescription(),
                props.getVersion(),
                props.getTermsOfServiceUrl(),
                new Contact(props.getContactName(), props.getContactUrl(), props.getContactEmail()),
                props.getLicense(),
                props.getLicenseUrl(),
                Collections.emptyList());
    }

}
