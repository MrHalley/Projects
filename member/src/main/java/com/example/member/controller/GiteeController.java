package com.example.member.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.member.entity.GiteeEntity;
import com.example.member.service.GiteeService;
import com.example.common.utils.PageUtils;
import com.example.common.utils.R;



/**
 * gitee账号
 *
 * @author 杜延文
 * @email 1165449791@qq.com
 * @date 2022-09-05 22:44:17
 */
@RestController
@RequestMapping("member/gitee")
public class GiteeController {
    @Autowired
    private GiteeService giteeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = giteeService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		GiteeEntity gitee = giteeService.getById(id);

        return R.ok().put("gitee", gitee);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody GiteeEntity gitee){
		giteeService.save(gitee);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody GiteeEntity gitee){
		giteeService.updateById(gitee);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		giteeService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
