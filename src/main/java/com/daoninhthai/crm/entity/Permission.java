package com.daoninhthai.crm.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String module;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PermissionAction action;

    public enum PermissionAction {
        READ, CREATE, UPDATE, DELETE, EXPORT, IMPORT, MANAGE
    }
}
