package com.example.product.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.utils.PageUtils;
import com.example.product.entity.CategoryEntity;
import com.example.product.vo.Catelog2Vo;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author 杜延文
 * @email 1165449791@qq.com
 * @date 2022-05-24 22:40:36
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    Long[] findCatelogPath(Long catelogId);

    void updateCascade(CategoryEntity category);

    List<CategoryEntity> getLevel1Categorys();

    Map<String, List<Catelog2Vo>> getCatalogJsonFromDb();

    Map<String, List<Catelog2Vo>> getCatalogJsonFromDbWithLocalLock();

    Map<String, List<Catelog2Vo>> getCatalogJsonFromDbWithRedisLock();

    Map<String, List<Catelog2Vo>> getCatalogJson();
}

