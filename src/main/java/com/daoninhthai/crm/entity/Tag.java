package com.daoninhthai.crm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String color;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TagType type;

    @ManyToMany(mappedBy = "tags")
    @Builder.Default
    private Set<Contact> contacts = new HashSet<>();

    @ManyToMany(mappedBy = "tags")
    @Builder.Default
    private Set<Deal> deals = new HashSet<>();
}
