package com.example.member.dao;

import com.example.member.entity.GiteeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * gitee账号
 * 
 * @author 杜延文
 * @email 1165449791@qq.com
 * @date 2022-09-05 22:44:17
 */
@Mapper
public interface GiteeDao extends BaseMapper<GiteeEntity> {
	
}
