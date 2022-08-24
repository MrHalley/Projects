package com.example.search.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 杜延文
 * @version 1.0
 * @date 2022/8/24 15:30
 */
@Controller
public class SearchController {

    @GetMapping(value = {"/","/search.html"})
    public String search(){
        return "search";
    }
}
