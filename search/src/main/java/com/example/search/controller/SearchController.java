package com.example.search.controller;

import com.example.search.service.MallSearchService;
import com.example.search.vo.SearchParam;
import com.example.search.vo.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 杜延文
 * @version 1.0
 * @date 2022/8/24 15:30
 */
@Slf4j
@Controller
public class SearchController {

    @Resource
    private MallSearchService mallSearchService;

    @GetMapping(value = {"/","/search.html"})
    public String listPage(SearchParam param, Model model, HttpServletRequest request) {

        param.set_queryString(request.getQueryString());

        //1、根据传递来的页面的查询参数，去es中检索商品
        SearchResult result = mallSearchService.search(param);

        log.info("result={}",result);
        model.addAttribute("result",result);
        return "search";
    }
}
