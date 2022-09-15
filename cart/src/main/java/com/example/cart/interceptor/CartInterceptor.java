package com.example.cart.interceptor;

import com.example.cart.to.UserInfoTo;
import com.example.common.constant.AuthServerConstant;
import com.example.common.constant.CartConstant;
import com.example.common.vo.MemberResponseVo;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

import static com.example.common.constant.CartConstant.TEMP_USER_COOKIE_NAME;
import static com.example.common.constant.CartConstant.TEMP_USER_COOKIE_TIMEOUT;

/**
 * @author 杜延文
 * @version 1.0
 * @date 2022/9/13 16:32
 */
public class CartInterceptor implements HandlerInterceptor {
    public static ThreadLocal<UserInfoTo> toThreadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserInfoTo userInfoTo = new UserInfoTo();
        //获取登录用户
        HttpSession session = request.getSession();
        MemberResponseVo memberResponseVo = (MemberResponseVo)session.getAttribute(AuthServerConstant.LOGIN_USER);
        if(memberResponseVo != null){
            userInfoTo.setUserId(memberResponseVo.getId());
        }
        //获取临时用户
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length != 0)
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(TEMP_USER_COOKIE_NAME)){
                    userInfoTo.setUserKey(cookie.getValue());
                    userInfoTo.setTempUser(true);
                }
            }

        //临时用户不存在时创建临时用户token
        if(StringUtils.isEmpty(userInfoTo.getUserKey())){
            String uuid = UUID.randomUUID().toString();
            userInfoTo.setUserKey(uuid);
        }

        toThreadLocal.set(userInfoTo);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserInfoTo userInfoTo = toThreadLocal.get();
        if(!userInfoTo.getTempUser()){
            Cookie cookie = new Cookie(TEMP_USER_COOKIE_NAME, userInfoTo.getUserKey());
            cookie.setDomain("mall.com");
            cookie.setMaxAge(TEMP_USER_COOKIE_TIMEOUT);
            response.addCookie(cookie);
        }
    }
}
