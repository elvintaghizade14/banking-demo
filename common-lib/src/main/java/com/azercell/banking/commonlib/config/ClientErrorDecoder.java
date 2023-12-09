package com.azercell.banking.commonlib.config;

import com.azercell.banking.commonlib.exception.ClientException;
import com.azercell.banking.commonlib.exception.model.CommonErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

@ConditionalOnBean(value = FeignConfig.class)
public class ClientErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;
    private final ErrorDecoder defaultErrorDecoder;

    public ClientErrorDecoder(ObjectMapper objectMapper) {
        this.defaultErrorDecoder = new Default();
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            CommonErrorResponse error
                    = objectMapper.readValue(response.body().asInputStream(), CommonErrorResponse.class);
            return new ClientException(response.status(), error.getRequestId(),
                    error.getErrorCode(), error.getMessage());
        } catch (Exception ex) {
            return defaultErrorDecoder.decode(methodKey, response);
        }
    }

}
