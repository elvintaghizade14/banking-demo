package com.azercell.banking.commonlib.model.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonConstants {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class HttpAttribute {
        public static final String REQUEST_ID = "request_id";
        public static final String REQUEST_START_TIME = "request_start_time";
        public static final String BEARER = "Bearer ";
        public static final String BASIC = "Basic ";
        public static final String CONTENT_TYPE = "Content-Type";
        public static final String ACCEPT = "Accept";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class HttpHeader {
        public static final String X_FORWARDED_FOR = "X-Forwarded-For";
        public static final String X_USER_ID = "X-User-ID";
        public static final String X_MOBILE_NUMBER = "X-Mobile-Number";
        public static final String X_USERNAME = "X-Username";
        public static final String X_USER_ROLE = "X-User-Role";
        public static final String X_USER_STATUS = "X-User-Status";
        public static final String X_REQUEST_ID = "X-Request-ID";
        public static final String AUTHORIZATION = "Authorization";
    }

}
