package com.azercell.banking.commonlib.exception.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommonErrorResponse {

    private String requestId;
    private String errorCode;
    private String message;

}
