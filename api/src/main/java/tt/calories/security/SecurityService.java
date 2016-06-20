/*
 * Copyright (c) 2016. Cloves Almeida. All rights reserved.
 */

package tt.calories.security;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.IterableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tt.calories.domain.User;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

/**
 * Bean with security-related methods. Most commonly used in Spring EL expression using
 * the @PreAuthorize and @PostAuthorize annotations in controllers/repositories.
 *
 * @author Cloves J. G. Almeida <cjalmeida@gmail.com>
 * @version 1.0.0
 */
@Service("sec")
public class SecurityService {

    @Autowired
    EntityManager entityManager;

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }

    public boolean isManager() {
        return hasRole("ROLE_MANAGER");
    }

    public boolean hasRole(String role) {
        return getAuthentication()
            .getAuthorities()
            .stream()
            .filter((a) -> a.getAuthority().equals(role))
            .findAny()
            .isPresent();
    }

    public User getCurrentUser() {
        return (User) getAuthentication().getDetails();
    }

    public Long getUserId() {
        return getCurrentUser().getId();
    }

    /**
     * Check if the logged in user is and admin or userId equals to
     * the logged user id.
     */
    public boolean checkUser(Long userId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) return false;
        return isAdmin() || userId.equals(getCurrentUser().getId());
    }

    /**
     * Check if the logged in user is and admin or ALL userIds equals to
     * the logged user id.
     */
    public boolean checkUser(Iterable<? extends Long> userIds) {
        User currentUser = getCurrentUser();
        if (currentUser == null) return false;

        if (isAdmin()) return true;

        Long currentUserId = currentUser.getId();
        for (Long id : userIds) {
            if (!id.equals(currentUserId)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Loads the entity and check if the same user. We navigate the object graph
     * using the userIdPath string.
     */
    public boolean checkUser(Class<?> clazz, Serializable entityId, String userIdPath) {
        Object entity = entityManager.find(clazz, entityId);
        try {
            Long userId = (Long) PropertyUtils.getProperty(entity, userIdPath);
            return checkUser(userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Shortcut to {@link #checkUser(Class, Serializable, String)} where
     * the simple-name of a class in tt.calories.domain is passed and userIdPath == 'user.id'
     */
    public boolean checkUser(String domainClassName, Serializable entityId) {
        try {
            Class<?> clazz = Class.forName("tt.calories.domain." + domainClassName);
            return checkUser(clazz, entityId, "user.id");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


}
