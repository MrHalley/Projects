package com.example.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.utils.PageUtils;
import com.example.member.entity.GiteeEntity;

import java.util.Map;

/**
 * gitee账号
 *
 * @author 杜延文
 * @email 1165449791@qq.com
 * @date 2022-09-05 22:44:17
 */
public interface GiteeService extends IService<GiteeEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

