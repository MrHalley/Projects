package com.example.order.feign;

import com.example.common.utils.R;
import com.example.order.vo.WareSkuLockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author 杜延文
 * @version 1.0
 * @date 2022/10/13 9:13
 */
@FeignClient("mall-ware")
public interface WmsFeignService {


    @PostMapping(value = "/ware/waresku/hasStock")
    R getSkuHasStock(@RequestBody List<Long> skuIds);


    /**
     * 查询运费和收货地址信息
     * @param addrId
     * @return
     */
    @GetMapping(value = "/ware/wareinfo/fare")
    R getFare(@RequestParam("addrId") Long addrId);

    /**
     * 锁定库存
     * @param lockVo
     * @return
     */
    @PostMapping("/ware/waresku/lock/order")
    R orderLockStock(WareSkuLockVo lockVo);
}
