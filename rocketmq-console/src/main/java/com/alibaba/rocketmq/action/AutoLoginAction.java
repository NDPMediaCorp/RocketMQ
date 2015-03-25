package com.alibaba.rocketmq.action;

import com.ndpmedia.rocketmq.cockpit.model.CockpitRole;
import com.ndpmedia.rocketmq.cockpit.model.Login;
import com.ndpmedia.rocketmq.cockpit.mybatis.mapper.LoginMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * console auto login.
 * must login in cockpit .
 */
@Controller
@RequestMapping("/authority")
public class AutoLoginAction {

    @Autowired
    private AuthenticationManager myAuthenticationManager;

    @Autowired
    private LoginMapper loginMapper;

    @RequestMapping(value = "/login.do", method = {RequestMethod.GET, RequestMethod.POST})
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {

        boolean hasLoggedIn = false;
        try {
            UsernamePasswordAuthenticationToken token = getToken(request);
            if (null != token) {
                token.setDetails(new WebAuthenticationDetails(request));
                Authentication authenticatedUser = myAuthenticationManager.authenticate(token);
                SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
                response.sendRedirect("../cluster/list.do");
                hasLoggedIn = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!hasLoggedIn) {
                response.sendRedirect(
                        request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/");
            }
        }
    }

    private UsernamePasswordAuthenticationToken getToken(HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        String sessionId = session.getId();

        Login login =  loginMapper.get(sessionId);

        if (null == login) {
            return null;
        }

        return new UsernamePasswordAuthenticationToken(login.getCockpitUser().getUsername(),
                login.getCockpitUser().getPassword(), wrap(login.getCockpitUser().getCockpitRoles()));
    }

    private Collection<? extends GrantedAuthority> wrap(List<CockpitRole> cockpitRoles) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(cockpitRoles.size());
        for (CockpitRole role : cockpitRoles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }
}
