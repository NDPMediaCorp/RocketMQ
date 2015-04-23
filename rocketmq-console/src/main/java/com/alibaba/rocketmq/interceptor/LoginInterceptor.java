package com.alibaba.rocketmq.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    /**
     * This implementation always returns {@code true}.
     *
     * @param request
     * @param response
     * @param handler
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        //Check if the request is a callback from SSO.
        if (Helper.CALLBACK_URL.equals(request.getRequestURI())) {
            String token = request.getParameter(Helper.TOKEN_KEY);
            if (null == token || token.isEmpty()) {
                response.sendRedirect("");
            }
        }


        //Check if already logged in.
        HttpSession session = request.getSession();
        if (null == session.getAttribute(Helper.LOGIN_KEY)) {


        }

        return super.preHandle(request, response, handler);
    }
}
