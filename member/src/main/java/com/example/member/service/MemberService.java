package com.example.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.utils.PageUtils;
import com.example.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author 杜延文
 * @email 1165449791@qq.com
 * @date 2022-05-24 23:48:33
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

