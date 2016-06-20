/*
 * Copyright (c) 2016. Cloves Almeida. All rights reserved.
 */

package tt.calories.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import tt.calories.domain.Role;
import tt.calories.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for authenticating and granting authorities to an user.
 *
 * @author Cloves J. G. Almeida <cjalmeida@gmail.com>
 * @version 1.0.0
 */
@Component
public class AuthenticationManagerImpl implements AuthenticationManager {

    @Autowired
    EntityManager entityManager;


    /**
     * This methods authenticates and grant authorities.
     * <p>
     * <code>null</code> is returned.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // Fetch user
        User user;
        try {
            user = entityManager
                .createQuery("from User as u where email = :email", User.class)
                .setParameter("email", authentication.getPrincipal())
                .getSingleResult();
            entityManager.detach(user);
        } catch (NoResultException e) {
            // User not found.
            return null;
        }

        // Try password based authentication if not authenticated.
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            authentication = passwordAuthenticate(user, authentication);
            if (authentication == null)
                return null;
        } else if (authentication instanceof PreAuthenticatedAuthenticationToken) {
            // Do nothing
        } else {
            return null;
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
            authentication.getPrincipal(),
            authentication.getCredentials(),
            authorities);

        auth.setDetails(user);

        return auth;
    }

    /**
     * Authenticates with password as credentials.
     *
     * @return the authentication object or null if failed authentication.
     */
    private Authentication passwordAuthenticate(User user, Authentication authentication) {
        String hashedPass = PasswordHasher.hash((String) authentication.getCredentials(), user.getSalt());
        if (user.getPassword().equals(hashedPass)) {
            return authentication;
        } else {
            return null;
        }
    }

}
