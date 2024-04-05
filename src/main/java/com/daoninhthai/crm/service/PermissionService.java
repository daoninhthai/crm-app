package com.daoninhthai.crm.service;

import com.daoninhthai.crm.entity.Permission;
import com.daoninhthai.crm.entity.RolePermission;
import com.daoninhthai.crm.entity.User;
import com.daoninhthai.crm.repository.PermissionRepository;
import com.daoninhthai.crm.repository.RolePermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Transactional(readOnly = true)
    public boolean hasPermission(User.UserRole role, String permissionName) {
        // ADMIN has all permissions
        if (role == User.UserRole.ADMIN) {
            return true;
        }
        return rolePermissionRepository.hasPermission(role, permissionName);
    }

    @Transactional(readOnly = true)
    public List<Permission> getPermissionsForRole(User.UserRole role) {
        return rolePermissionRepository.findByRoleWithPermissions(role).stream()
                .map(RolePermission::getPermission)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @Transactional
    public Permission createPermission(Permission permission) {
        if (permissionRepository.existsByName(permission.getName())) {
            throw new IllegalArgumentException("Permission already exists: " + permission.getName());
        }
        return permissionRepository.save(permission);
    }

    @Transactional
    public void grantPermission(User.UserRole role, Long permissionId) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new IllegalArgumentException("Permission not found: " + permissionId));

        if (rolePermissionRepository.hasPermission(role, permission.getName())) {
            log.info("Role {} already has permission {}", role, permission.getName());
            return;
        }

        RolePermission rp = RolePermission.builder()
                .role(role)
                .permission(permission)
                .build();
        rolePermissionRepository.save(rp);
    }

    @Transactional
    public void revokePermission(User.UserRole role, Long permissionId) {
        rolePermissionRepository.deleteByRoleAndPermissionId(role, permissionId);
    }
}
