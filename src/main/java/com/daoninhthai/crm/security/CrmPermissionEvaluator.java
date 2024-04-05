package com.daoninhthai.crm.security;

import com.daoninhthai.crm.entity.User;
import com.daoninhthai.crm.repository.UserRepository;
import com.daoninhthai.crm.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@RequiredArgsConstructor
@Slf4j
public class CrmPermissionEvaluator implements PermissionEvaluator {

    private final PermissionService permissionService;
    private final UserRepository userRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || permission == null) {
            return false;
        }

        String username = authentication.getName();
        String permissionName = permission.toString();

        return checkPermission(username, permissionName);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId,
                                  String targetType, Object permission) {
        if (authentication == null || permission == null) {
            return false;
        }

        String username = authentication.getName();
        String permissionName = targetType + ":" + permission.toString();

        return checkPermission(username, permissionName);
    }

    private boolean checkPermission(String username, String permissionName) {
        User user = userRepository.findByEmail(username).orElse(null);
        if (user == null) {
            log.warn("User not found for permission check: {}", username);
            return false;
        }

        boolean hasPermission = permissionService.hasPermission(user.getRole(), permissionName);
        log.debug("Permission check: user={}, permission={}, result={}", username, permissionName, hasPermission);
        return hasPermission;
    }
}
