package com.example.authserver.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.authserver.feign.MemberFeignService;
import com.example.common.vo.GiteeToken;
import com.example.common.utils.HttpUtils;
import com.example.common.utils.R;
import com.example.common.vo.MemberResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

import static com.example.common.constant.AuthServerConstant.LOGIN_USER;


@Slf4j
@Controller
public class OAuth2Controller {

    @Autowired
    private MemberFeignService memberFeignService;


    @GetMapping(value = "/oauth2.0/gitee/success")
    public String weibo(@RequestParam("code") String code, HttpSession session) throws Exception {
        if(session.getAttribute(LOGIN_USER) != null){
            return "redirect:http://mall.com";
        }

        Map<String, String> map = new HashMap<>();
        map.put("client_id","65156c8b1a06dc06fc7c8ae50c8c5494c03e857e1833cf6d8c3f24e74a11b09a");
        map.put("client_secret","71471d4200d3d65c9b676cdb5214a4ff70c0aa6d43a775fe246e76fe3414078f");
        map.put("grant_type","authorization_code");
        map.put("redirect_uri","http://auth.mall.com/oauth2.0/gitee/success");
        map.put("code",code);

        //1、根据用户授权返回的code换取access_token
        HttpResponse response = HttpUtils.doPost("https://gitee.com", "/oauth/token", "post", new HashMap<>(), map, new HashMap<>());

        System.out.println("response = " + response);
        //2、处理
        if (response.getStatusLine().getStatusCode() == 200) {
            //获取到了access_token,转为通用社交登录对象
            String json = EntityUtils.toString(response.getEntity());
            GiteeToken giteeToken = JSON.parseObject(json, GiteeToken.class);
//知道了哪个社交用户
            //1）、当前用户如果是第一次进网站，自动注册进来（为当前社交用户生成一个会员信息，以后这个社交账号就对应指定的会员）
            //登录或者注册这个社交用户
            System.out.println(giteeToken.getAccess_token());
            //调用远程服务
            R oauthLogin = memberFeignService.giteeLogin(giteeToken);
            if (oauthLogin.getCode() == 0) {
                MemberResponseVo data = oauthLogin.getData("data", new TypeReference<MemberResponseVo>() {});
                log.info("登录成功：用户信息：{}",data.toString());

                //1、第一次使用session，命令浏览器保存卡号，JSESSIONID这个cookie
                //以后浏览器访问哪个网站就会带上这个网站的cookie
                //TODO 1、默认发的令牌。当前域（解决子域session共享问题）
                //TODO 2、使用JSON的序列化方式来序列化对象到Redis中
                session.setAttribute(LOGIN_USER,data);

                //2、登录成功跳回首页
                return "redirect:http://mall.com";
            } else {
                return "redirect:http://auth.mall.com/login.html";
            }
            //String json = JSON.toJSONString(response.getEntity());
        } else {
            return "redirect:http://auth.mall.com/login.html";
        }
    }

}
