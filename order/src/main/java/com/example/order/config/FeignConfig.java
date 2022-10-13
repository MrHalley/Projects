package com.example.order.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: feign拦截器功能
 *
 * 问题：
 *      feign会发起请求时会单独创建新的请求（新请求不包含原始请求的header和queries等信息），将导致调用购物车服务时判定为没有用户登录而，
 *      无法查询当前用户的购物车信息（需要通过header中的cookie定位到session然后）
 * 源码阅读：
 *    1.通过debug Feign的远程调用定位到ReflectiveFeign.class文件的invoke方法（可以看出是重新创建新的请求）
 *    2.定位到SynchronousMethodHandler targetRequest(RequestTemplate template)发现将通过一系列拦截器（requestInterceptors）对新请求做增强
 *    3.定位FeignClientFactoryBean configureUsingConfiguration 发现会从容器中取出对应的拦截器到requestInterceptors中
 * 解决方案：
 *    参考RequestInterceptor之类BasicAuthRequestInterceptor，并实现自己的需要的拦截器（将老请求的cookie给到新请求中），并注入Spring容器即可
 **/

@Slf4j
@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        RequestInterceptor requestInterceptor = template -> {
            log.info("线程id:{}",Thread.currentThread().getId());
            //1、使用RequestContextHolder拿到刚进来的请求数据
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                //老请求
                HttpServletRequest request = requestAttributes.getRequest();
                if (request != null) {
                    //2、同步请求头的数据（主要是cookie）
                    //把老请求的cookie值放到新请求上来，进行一个同步
                    String cookie = request.getHeader("Cookie");
                    template.header("Cookie", cookie);
                }
            }
        };
        return requestInterceptor;
    }

}
