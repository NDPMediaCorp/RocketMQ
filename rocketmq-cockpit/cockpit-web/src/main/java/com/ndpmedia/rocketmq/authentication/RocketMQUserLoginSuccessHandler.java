package com.ndpmedia.rocketmq.authentication;

import com.ndpmedia.rocketmq.cockpit.model.CockpitUser;
import com.ndpmedia.rocketmq.cockpit.model.Login;
import com.ndpmedia.rocketmq.cockpit.mybatis.mapper.LoginMapper;
import com.ndpmedia.rocketmq.cockpit.util.LoginConstant;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * try to save user login session.
 */
public class RocketMQUserLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler
        implements LoginConstant {

    private final Logger logger = LoggerFactory.getLogger(RocketMQUserLoginSuccessHandler.class);


    @Autowired
    private LoginMapper loginMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(COCKPIT_USER_KEY, authentication.getPrincipal());
        Object principal = authentication.getPrincipal();

        if (principal instanceof CockpitUser) {
            CockpitUser cockpitUser = (CockpitUser)principal;
            Login login = new Login();
            login.setCockpitUser(cockpitUser);
            login.setLoginTime(new Date());
            login.setToken(UUID.randomUUID().toString().replace("-", ""));
            loginMapper.insert(login);
            logger.info("Account {Team: " + cockpitUser.getTeam().getName() +
                    ", Member: " + cockpitUser.getUsername() + "} logs in");
            httpSession.removeAttribute(LoginConstant.LOGIN_SESSION_ERROR_KEY);


            String redirectURL = request.getParameter(LoginConstant.REDIRECT_KEY);
            if (null != redirectURL && redirectURL.trim().length() > 0) {
                byte[] redirectBytes = Base64.decodeBase64(redirectURL);
                StringBuilder stringBuilder = new StringBuilder(redirectBytes.length * 2);
                String redirect = new String(redirectBytes, "UTF-8");
                stringBuilder.append(redirect);
                if (!redirect.contains("?")) {
                    stringBuilder.append("?");
                } else {
                    stringBuilder.append("&");
                }
                stringBuilder.append("token=").append(login.getToken())
                        .append("&").append(LoginConstant.REDIRECT_KEY).append("=").append(redirectURL);
                response.sendRedirect(stringBuilder.toString());
                return;
            }

        } else {
            logger.error("Fatal error, principal should be a CockpitUser or sub-class instance");
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
