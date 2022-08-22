package com.example.product;

import com.example.product.entity.BrandEntity;
import com.example.product.service.BrandService;
import com.example.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class ProductApplicationTests {
    @Resource
    BrandService brandService;
    @Resource
    CategoryService categoryService;
    @Test
    public void testFindPath(){
        Long[] catelogPath = categoryService.findCatelogPath(225L);
        log.info("完整路径：{}", Arrays.asList(catelogPath));
    }
    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("华为");
        brandService.save(brandEntity);
        System.out.println("保存成功...");
    }

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedisTemplate(){
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        stringStringValueOperations.setIfAbsent("hello", "hello_"+UUID.randomUUID());
        String hello = stringStringValueOperations.get("hello");
        System.out.println("valueOperations = " + hello);
    }
}
