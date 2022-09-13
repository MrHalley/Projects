package com.example.testssoclient.controller;

import com.example.testssoclient.constant.SsoContant;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 杜延文
 * @version 1.0
 * @date 2022/9/9 11:02
 */
@Controller
public class HelloController {


    @GetMapping("/hello")
    @ResponseBody
    public String hello(){
        return "hello";
    }

    @GetMapping("/employees")
    public String getEmployees(Model model, HttpServletRequest request, HttpSession httpSession, @RequestParam(value = "token",required = false) String token){
        if(!StringUtils.isEmpty(token)){
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> forEntity = restTemplate.getForEntity("http://sso.server.com:8080/userInfo?token=" + token, String.class);
            String body = forEntity.getBody();
            httpSession.setAttribute(SsoContant.LOGIN_USER,body);
        }
        Object loginUser = httpSession.getAttribute(SsoContant.LOGIN_USER);
        if(loginUser == null){
            StringBuffer url = request.getRequestURL();
            return "redirect:http://sso.server.com:8080/login.html?redirect_url="+url;
        }
        List<String> employees = new ArrayList<>();
        employees.add("张三");
        employees.add("李四");

        model.addAttribute("emps",employees);
        return "employees";
    }
}
