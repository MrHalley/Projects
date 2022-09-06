package com.example.member.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;
import com.example.common.utils.Query;

import com.example.member.dao.GiteeDao;
import com.example.member.entity.GiteeEntity;
import com.example.member.service.GiteeService;


@Service("giteeService")
public class GiteeServiceImpl extends ServiceImpl<GiteeDao, GiteeEntity> implements GiteeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<GiteeEntity> page = this.page(
                new Query<GiteeEntity>().getPage(params),
                new QueryWrapper<GiteeEntity>()
        );

        return new PageUtils(page);
    }

}