package com.azercell.banking.identity.service;

import com.azercell.banking.identity.config.properties.ApplicationProperties;
import com.azercell.banking.identity.model.dto.TokenPairDto;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Log4j2
@Service
public class RedisService {

    private final RedissonClient redissonClient;
    private final ApplicationProperties.Redis redisProps;

    public RedisService(RedissonClient redissonClient, ApplicationProperties properties) {
        this.redissonClient = redissonClient;
        this.redisProps = properties.getRedis();
    }

    public void save(String name, TokenPairDto tokenPairDto) {
        saveOrUpdate(name, tokenPairDto);
    }

    public void update(String name, TokenPairDto tokenPairDto) {
        saveOrUpdate(name, tokenPairDto);
    }

    public TokenPairDto read(String name) {
        RBucket<TokenPairDto> bucket = redissonClient.getBucket(name);
        return bucket.get();
    }

    public void delete(String name) {
        RBucket<TokenPairDto> bucket = redissonClient.getBucket(name);

        if (Objects.isNull(bucket)) {
            log.warn("{} bucket not found", name);
            return;
        }

        bucket.delete();
    }

    private void saveOrUpdate(String name, TokenPairDto tokenPairDto) {
        RBucket<TokenPairDto> bucket = redissonClient.getBucket(name);
        bucket.set(tokenPairDto, redisProps.getTokenTimeToLive(), TimeUnit.SECONDS);
    }

}
