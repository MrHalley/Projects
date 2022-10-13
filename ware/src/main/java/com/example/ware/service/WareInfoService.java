package com.example.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.utils.PageUtils;
import com.example.ware.entity.WareInfoEntity;
import com.example.ware.vo.FareVo;

import java.util.Map;

/**
 * 仓库信息
 *
 * @author 杜延文
 * @email 1165449791@qq.com
 * @date 2022-05-25 00:09:45
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取运费和收货地址信息
     * @param addrId
     * @return
     */
    FareVo getFare(Long addrId);
}

