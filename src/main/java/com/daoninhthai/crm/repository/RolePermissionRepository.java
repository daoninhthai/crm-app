package com.daoninhthai.crm.repository;

import com.daoninhthai.crm.entity.RolePermission;
import com.daoninhthai.crm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    List<RolePermission> findByRole(User.UserRole role);

    @Query("SELECT rp FROM RolePermission rp JOIN FETCH rp.permission WHERE rp.role = :role")
    List<RolePermission> findByRoleWithPermissions(@Param("role") User.UserRole role);

    @Query("SELECT CASE WHEN COUNT(rp) > 0 THEN true ELSE false END " +
           "FROM RolePermission rp WHERE rp.role = :role AND rp.permission.name = :permissionName")
    boolean hasPermission(@Param("role") User.UserRole role, @Param("permissionName") String permissionName);

    void deleteByRoleAndPermissionId(User.UserRole role, Long permissionId);
}
