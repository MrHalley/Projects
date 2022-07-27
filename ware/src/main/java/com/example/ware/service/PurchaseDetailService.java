package com.example.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.utils.PageUtils;
import com.example.ware.entity.PurchaseDetailEntity;

import java.util.Map;

/**
 * 
 *
 * @author 杜延文
 * @email 1165449791@qq.com
 * @date 2022-05-25 00:09:45
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

