package com.example.ware.dao;

import com.example.ware.entity.PurchaseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采购信息
 * 
 * @author 杜延文
 * @email 1165449791@qq.com
 * @date 2022-05-25 00:09:45
 */
@Mapper
public interface PurchaseDao extends BaseMapper<PurchaseEntity> {
	
}
