package com.example.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 想要远程调用别的服务
 *  1）、引入open-feign
 *  2）、编写一个接口，告诉SpringCloud这个接口需要远程调用
 *      a）、声明接口的每一个方法都是调用远程服务的请求
 *  3）、开启远程调用功能
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients("com.example.member.feign")
public class MemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemberApplication.class, args);
    }

}
