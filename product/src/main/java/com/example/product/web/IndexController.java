package com.example.product.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.product.entity.CategoryEntity;
import com.example.product.service.CategoryService;
import com.example.product.vo.Catelog2Vo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
public class IndexController {

    @Resource
    private CategoryService categoryService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping(value = {"/","index.html"})
    private String indexPage(Model model) {
        //1、查出所有的一级分类
        List<CategoryEntity> categoryEntities = categoryService.getLevel1Categorys();
        model.addAttribute("categories",categoryEntities);
        return "index";
    }


    /**
     * 加redis缓存，提升吞吐量
     *
     * @return
     */
    @GetMapping(value = "/index/catalog.json")
    @ResponseBody
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        Map<String, List<Catelog2Vo>> catalogJson = categoryService.getCatalogJson();
        return catalogJson;
    }

    @GetMapping(value = "/hello")
    @ResponseBody
    public String testJMeter(){
        return "hello";
    }


    @Resource
    private RedissonClient redissonClient;
    /**
     * 可重入锁，如果没有设置默认过期时间会有触发看门狗机制（自动延续过期时间）
     */
    @ResponseBody
    @GetMapping("/reentrantLock")
    public String redissonReentrantLock(){
        RLock reentrantLock = redissonClient.getLock("reentrantLock");
        reentrantLock.lock();
        try {
            log.debug("业务代码执行中..."+Thread.currentThread().getId());
            Thread.sleep(TimeUnit.SECONDS.toMillis(30));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            log.debug("释放锁"+Thread.currentThread().getId());
            reentrantLock.unlock();
        }
        //reentrantLock.lock();
        return "hello";
    }


    @ResponseBody
    @GetMapping("write")
    public String write(){
        RReadWriteLock rwLock = redissonClient.getReadWriteLock("rwLock");
        RLock writeLock = rwLock.writeLock();
        try {
            writeLock.lock();
            log.debug("加写锁完成,写数据中..."+Thread.currentThread().getId());
            Thread.sleep(TimeUnit.SECONDS.toMillis(30));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            log.debug("写数据完成，释放写锁"+Thread.currentThread().getId());
            writeLock.unlock();
        }
        return "write";
    }

    @ResponseBody
    @GetMapping("read")
    public String read(){
        RReadWriteLock rwLock = redissonClient.getReadWriteLock("rwLock");
        RLock readLock = rwLock.readLock();
        try {
            readLock.lock();
            log.debug("加读锁完成，读数据中..."+Thread.currentThread().getId());
            Thread.sleep(TimeUnit.SECONDS.toMillis(30));
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            log.debug("读数据完成，释放读锁"+Thread.currentThread().getId());
            readLock.unlock();
        }
        return "read";
    }

    /**
     * 闭锁
     * @return
     */
    @GetMapping("/setLatch")
    @ResponseBody
    public String setLatch() {
        RCountDownLatch latch = redissonClient.getCountDownLatch("CountDownLatch");
        try {
            latch.trySetCount(5);
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "人已走完，关门";
    }

    @GetMapping("/offLatch")
    @ResponseBody
    public String offLatch() {
        RCountDownLatch latch = redissonClient.getCountDownLatch("CountDownLatch");
        latch.countDown();
        return latch.getCount()>0?"还有"+latch.getCount()+"人":"人已走完";
    }


    /**
     * 信号量
     * @return
     */
    @GetMapping("/park")
    @ResponseBody
    public String park() {
        RSemaphore park = redissonClient.getSemaphore("park");
        try {
            park.acquire(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "停进2";
    }

    @GetMapping("/go")
    @ResponseBody
    public String go() {
        RSemaphore park = redissonClient.getSemaphore("park");
        park.release(2);
        return "开走2";
    }
}
