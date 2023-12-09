package com.azercell.banking.bffweb.model.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenKey {

    public static final String ID = "id";
    public static final String USER_ROLE = "user_role";
    public static final String USER_STATUS = "user_status";
    public static final String TOKEN_TYPE = "token_type";

}
