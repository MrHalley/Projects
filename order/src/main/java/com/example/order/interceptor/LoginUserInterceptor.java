package com.example.order.interceptor;

import com.example.common.vo.MemberResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Enumeration;

import static com.example.common.constant.AuthServerConstant.LOGIN_USER;

/**
 * 登录拦截器
 */
@Slf4j
@Component
public class LoginUserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MemberResponseVo loginUser = (MemberResponseVo)request.getSession().getAttribute(LOGIN_USER);
        if(loginUser != null){
            return true;
        }else{
            request.getSession().setAttribute("msg","请先进行登录");
            String requestURI = request.getRequestURI();
            String host = request.getHeader("x-forwarded-host");
            //当前地址，登录后跳回此地址
            String currentUrl = host + requestURI;
            response.sendRedirect("http://auth.mall.com/login.html?redirect_url="+currentUrl);
            return false;
        }
    }
}
