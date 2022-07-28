package com.example.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.example.product.entity.AttrEntity;
import com.example.product.service.AttrAttrgroupRelationService;
import com.example.product.service.AttrService;
import com.example.product.service.CategoryService;
import com.example.product.vo.AttrGroupRelationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.product.entity.AttrGroupEntity;
import com.example.product.service.AttrGroupService;
import com.example.common.utils.PageUtils;
import com.example.common.utils.R;

import javax.annotation.Resource;


/**
 * 属性分组
 *
 * @author 杜延文
 * @email 1165449791@qq.com
 * @date 2022-05-24 22:40:36
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private AttrService attrService;

    @Resource
    AttrAttrgroupRelationService relationService;

    /**
     * 添加分组属性关联
     *
     * @param vos
     * @return
     */
    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelationVo> vos){
        relationService.saveBatch(vos);
        return R.ok();
    }

    /**
     * 删除分组属性关联
     *
     * @param vos
     * @return
     */
    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody  AttrGroupRelationVo[] vos){
        attrService.deleteRelation(vos);
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    public R list(@RequestParam Map<String, Object> params,@PathVariable("catelogId") Long catelogId){
        PageUtils page = attrGroupService.queryPage(params,catelogId);
        return R.ok().put("page", page);
    }

    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrgroupId){
        List<AttrEntity> entities =  attrService.getRelationAttr(attrgroupId);
        return R.ok().put("data",entities);
    }

    @GetMapping("/{attrgroupId}/noattr/relation")
    public R attrNoRelation(@PathVariable("attrgroupId") Long attrgroupId,
                            @RequestParam Map<String, Object> params){
        PageUtils page = attrService.getNoRelationAttr(params,attrgroupId);
        return R.ok().put("page",page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long catelogId = attrGroup.getCatelogId();
        Long[] path = categoryService.findCatelogPath(catelogId);
        attrGroup.setCatelogPath(path);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
