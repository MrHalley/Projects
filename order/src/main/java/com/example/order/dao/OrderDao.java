package com.example.order.dao;

import com.example.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author 杜延文
 * @email 1165449791@qq.com
 * @date 2022-05-25 00:01:41
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
