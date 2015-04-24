package com.ndpmedia.rocketmq.cockpit.controller;

import com.ndpmedia.rocketmq.cockpit.util.LoginConstant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * the login controller.
 */
@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public String showLogin(HttpServletRequest request) {
        System.out.println(" first , we need open the login page .");

        String redirectURL = request.getParameter(LoginConstant.REDIRECT_KEY);
        if (null != redirectURL) {
            request.getSession().setAttribute(LoginConstant.REDIRECT_URL_IN_SESSION, redirectURL);
            System.out.println("Redirect URL in session set");
        } else {
            System.out.println("Redirect URL not found: " + request.getRequestURL());
        }

        return "login";
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String showHome() {
        return "home";
    }
}
