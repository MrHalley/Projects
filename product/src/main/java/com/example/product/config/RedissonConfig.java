package com.example.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RedissonConfig {
    /**
     * 所有对Redisson的操作都是通过redissonClient对象
     * @return
     */
    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://redis.mall.com:6379");
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
