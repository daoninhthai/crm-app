package com.daoninhthai.crm.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role_permissions", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"role", "permission_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private User.UserRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;
}
