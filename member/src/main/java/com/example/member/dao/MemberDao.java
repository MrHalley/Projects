package com.example.member.dao;

import com.example.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author 杜延文
 * @email 1165449791@qq.com
 * @date 2022-05-24 23:48:33
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
