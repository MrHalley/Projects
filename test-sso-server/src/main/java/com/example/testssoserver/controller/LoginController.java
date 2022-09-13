package com.example.testssoserver.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author 杜延文
 * @version 1.0
 * @date 2022/9/9 11:44
 */
@Controller
public class LoginController {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @GetMapping("/userInfo")
    @ResponseBody
    public String getUserInfo(@RequestParam("token") String token){
        String s = stringRedisTemplate.opsForValue().get(token);
        return s;
    }

    @GetMapping("/login.html")
    public String login(Model model, @RequestParam("redirect_url") String url,@CookieValue(value = "sso_token", required = false) String sso_token){
        if (!StringUtils.isEmpty(sso_token)) {
            return "redirect:" + url + "?token=" + sso_token;
        }
        model.addAttribute("url",url);
        return "login";
    }

    @PostMapping("/doLogin")
    public String doLogin(@RequestParam("username") String userName,
                          @RequestParam("password") String password,
                          @RequestParam("redirect_url") String url,
                          HttpServletResponse response){
        if(!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password)){
            String uuid = UUID.randomUUID().toString();
            stringRedisTemplate.opsForValue().set(uuid,userName);
            Cookie cookie = new Cookie("sso_token",uuid);
            response.addCookie(cookie);
            return "redirect:"+url+"?token="+uuid;
        }
        return "login";
    }

}
