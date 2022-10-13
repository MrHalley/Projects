package com.example.order.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.example.common.to.SkuHasStockVo;
import com.example.common.utils.R;
import com.example.common.vo.MemberResponseVo;
import com.example.order.feign.CartFeignService;
import com.example.order.feign.MemberFeignService;
import com.example.order.feign.WmsFeignService;
import com.example.order.interceptor.LoginUserInterceptor;
import com.example.order.vo.MemberAddressVo;
import com.example.order.vo.OrderConfirmVo;
import com.example.order.vo.OrderItemVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;
import com.example.common.utils.Query;

import com.example.order.dao.OrderDao;
import com.example.order.entity.OrderEntity;
import com.example.order.service.OrderService;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;


@Slf4j
@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    @Resource
    private MemberFeignService memberFeignService;
    @Resource
    private CartFeignService cartFeignService;
    @Resource
    private WmsFeignService wmsFeignService;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException {
        //构建OrderConfirmVo
        OrderConfirmVo confirmVo = new OrderConfirmVo();

        //获取当前用户登录的信息
        MemberResponseVo memberResponseVo = LoginUserInterceptor.loginUser.get();

        //TODO :获取当前线程请求头信息(解决Feign异步调用丢失请求头问题)
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        log.info("主线程id:{}",Thread.currentThread().getId());
        //开启第一个异步任务
        CompletableFuture<Void> addressFuture = CompletableFuture.runAsync(() -> {
            log.info("addressFuture线程id:{}",Thread.currentThread().getId());
            //每一个线程都来共享之前的请求数据
            RequestContextHolder.setRequestAttributes(requestAttributes);
            //1.查询所有的收获地址列表
            List<MemberAddressVo> address = memberFeignService.getAddress(memberResponseVo.getId());
            confirmVo.setMemberAddressVos(address);
        }, threadPoolExecutor);

        //开启第二个异步任务
        CompletableFuture<Void> cartInfoFuture = CompletableFuture.runAsync(() -> {
            log.info("addressFuture线程id:{}",Thread.currentThread().getId());
            //每一个线程都来共享之前的请求数据
            RequestContextHolder.setRequestAttributes(requestAttributes);
            //2、远程查询购物车所有选中的购物项
            List<OrderItemVo> currentCartItems = cartFeignService.getCurrentCartItems();
            confirmVo.setItems(currentCartItems);
            //feign在远程调用之前要构造请求，调用很多的拦截器
        }).thenRunAsync(()->{
            List<OrderItemVo> items = confirmVo.getItems();
            //获取全部商品的id
            List<Long> skuIds = items.stream()
                    .map((itemVo -> itemVo.getSkuId()))
                    .collect(Collectors.toList());
            //远程查询商品库存信息
            R skuHasStock = wmsFeignService.getSkuHasStock(skuIds);
            List<SkuHasStockVo> skuStockVos = skuHasStock.getData("data", new TypeReference<List<SkuHasStockVo>>() {});
            if (skuStockVos != null && skuStockVos.size() > 0) {
                //将skuStockVos集合转换为map
                Map<Long, Boolean> skuHasStockMap = skuStockVos.stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, SkuHasStockVo::getHasStock));
                confirmVo.setStocks(skuHasStockMap);
            }
        },threadPoolExecutor);


        //3、查询用户积分
        Integer integration = memberResponseVo.getIntegration();
        confirmVo.setIntegration(integration);

        //4、其它数据自动计算

        //TODO 5、防重令牌

        //所有异步任务执行完之前阻塞等待
        CompletableFuture.allOf(addressFuture, cartInfoFuture).get();
        return confirmVo;
    }
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

}