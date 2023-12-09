package com.azercell.banking.bffweb.service;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedissonClient redissonClient;
    
    public boolean read(String name) {
        return redissonClient.getBucket(name).isExists();
    }

}
