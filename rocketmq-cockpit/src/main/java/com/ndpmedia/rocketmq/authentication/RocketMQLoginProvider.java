package com.ndpmedia.rocketmq.authentication;

import com.ndpmedia.rocketmq.cockpit.model.CockpitUser;
import com.ndpmedia.rocketmq.cockpit.model.Status;
import com.ndpmedia.rocketmq.cockpit.mybatis.mapper.CockpitUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

public class RocketMQLoginProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CockpitUserMapper cockpitUserMapper;

    /**
     * Allows subclasses to perform any additional checks of a returned (or cached) <code>UserDetails</code>
     * for a given authentication request. Generally a subclass will at least compare the {@link
     * org.springframework.security.core.Authentication#getCredentials()} with a
     * {@link org.springframework.security.core.userdetails.UserDetails#getPassword()}. If custom logic is needed to compare
     * additional properties of <code>UserDetails</code> and/or <code>UsernamePasswordAuthenticationToken</code>,
     * these should also appear in this method.
     *
     * @param userDetails    as retrieved from the {@link #retrieveUser(String, org.springframework.security.authentication.UsernamePasswordAuthenticationToken)} or
     *                       <code>UserCache</code>
     * @param authentication the current request that needs to be authenticated
     * @throws org.springframework.security.core.AuthenticationException AuthenticationException if the credentials could not be validated (generally a
     *                                                                   <code>BadCredentialsException</code>, an <code>AuthenticationServiceException</code>)
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        if (null == authentication.getCredentials()) {
            throw new BadCredentialsException("Bad Credentials");
        }

        if (!passwordEncoder.matches(authentication.getCredentials().toString(), userDetails.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }

    }

    /**
     * Allows subclasses to actually retrieve the <code>UserDetails</code> from an implementation-specific
     * location, with the option of throwing an <code>AuthenticationException</code> immediately if the presented
     * credentials are incorrect (this is especially useful if it is necessary to bind to a resource as the user in
     * order to obtain or generate a <code>UserDetails</code>).<p>Subclasses are not required to perform any
     * caching, as the <code>AbstractUserDetailsAuthenticationProvider</code> will by default cache the
     * <code>UserDetails</code>. The caching of <code>UserDetails</code> does present additional complexity as this
     * means subsequent requests that rely on the cache will need to still have their credentials validated, even if
     * the correctness of credentials was assured by subclasses adopting a binding-based strategy in this method.
     * Accordingly it is important that subclasses either disable caching (if they want to ensure that this method is
     * the only method that is capable of authenticating a request, as no <code>UserDetails</code> will ever be
     * cached) or ensure subclasses implement {@link #additionalAuthenticationChecks(org.springframework.security.core.userdetails.UserDetails,
     * org.springframework.security.authentication.UsernamePasswordAuthenticationToken)} to compare the credentials of a cached <code>UserDetails</code> with
     * subsequent authentication requests.</p>
     * <p>Most of the time subclasses will not perform credentials inspection in this method, instead
     * performing it in {@link #additionalAuthenticationChecks(org.springframework.security.core.userdetails.UserDetails, org.springframework.security.authentication.UsernamePasswordAuthenticationToken)} so
     * that code related to credentials validation need not be duplicated across two methods.</p>
     *
     * @param username       The username to retrieve
     * @param authentication The authentication request, which subclasses <em>may</em> need to perform a binding-based
     *                       retrieval of the <code>UserDetails</code>
     * @return the user information (never <code>null</code> - instead an exception should the thrown)
     * @throws org.springframework.security.core.AuthenticationException if the credentials could not be validated (generally a
     *                                                                   <code>BadCredentialsException</code>, an <code>AuthenticationServiceException</code> or
     *                                                                   <code>UsernameNotFoundException</code>)
     */
    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        CockpitUser cockpitUser = cockpitUserMapper.get(-1, username);

        if (null == cockpitUser) {
            throw new BadCredentialsException("Credential error.");
        }

        if (cockpitUser.getStatus() != Status.ACTIVE) {
            throw new DisabledException("Inactive account");
        }

        return cockpitUser;
    }
}
