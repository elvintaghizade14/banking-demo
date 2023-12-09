package com.azercell.banking.bffweb.config;

import com.azercell.banking.bffweb.config.properties.ApplicationProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.SerializationCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

    private final ApplicationProperties.Redis redisProperties;

    public RedisConfig(ApplicationProperties properties) {
        this.redisProperties = properties.getRedis();
    }

    @Bean
    RedissonClient redissonClient() {
        final Config config = new Config();
        config.setCodec(new SerializationCodec());
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress(redisProperties.getAddress());
        singleServerConfig.setPassword(redisProperties.getPassword());
        singleServerConfig.setConnectionPoolSize(redisProperties.getConnectionPoolSize());
        singleServerConfig.setConnectionMinimumIdleSize(redisProperties.getConnectionMinimumIdleSize());
        return Redisson.create(config);
    }

}
