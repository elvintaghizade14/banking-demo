package com.azercell.banking.bffweb.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    private final Security security = new Security();
    private final Redis redis = new Redis();

    @Getter
    @Setter
    public static class Redis {
        private String address;
        private String password;
        private int connectionPoolSize;
        private int connectionMinimumIdleSize;
        private long tokenTimeToLive;
    }

    @Getter
    @Setter
    public static class Security {

        private final Authentication authentication = new Authentication();

        @Getter
        @Setter
        public static class Authentication {
            private String publicKey;
        }

    }

}
