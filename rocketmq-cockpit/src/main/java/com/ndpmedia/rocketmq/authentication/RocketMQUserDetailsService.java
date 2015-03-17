package com.ndpmedia.rocketmq.authentication;

import com.ndpmedia.rocketmq.cockpit.model.CockpitRole;
import com.ndpmedia.rocketmq.cockpit.model.CockpitUser;
import com.ndpmedia.rocketmq.cockpit.mybatis.mapper.CockpitUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * authentication.
 */
public class RocketMQUserDetailsService implements UserDetailsService {

    @Autowired
    private CockpitUserMapper cockpitUserMapper;

    private final Logger logger = LoggerFactory.getLogger(RocketMQUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserDetails user = null;

        logger.debug("[login] try to login ====userName===== " + userName);

        CockpitUser cockpitUser = cockpitUserMapper.get(-1, userName);
        if (cockpitUser == null) {
            throw new UsernameNotFoundException("Credentials incorrect.");
        }

        logger.debug("[login] try to login as " + cockpitUser.getUsername());

        // 用户名、密码、是否启用、是否被锁定、是否过期、权限
        user = new User(userName, cockpitUser.getPassword(), true, true, true, true,
                getAuthority(cockpitUser.getId()));

        return user;
    }

    /**
     * try to get the role of the user
     * @param userId User ID.
     * @return the role of user
     */
    public Collection<GrantedAuthority> getAuthority(long userId) {

        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();

        List<CockpitRole> roles = cockpitUserMapper.queryRoles(userId);
        for (CockpitRole role : roles) {
            authList.add(new SimpleGrantedAuthority(role.getName()));
        }

        return authList;
    }
}
