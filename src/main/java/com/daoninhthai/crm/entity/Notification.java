package com.daoninhthai.crm.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications", indexes = {
    @Index(name = "idx_notification_user", columnList = "user_id"),
    @Index(name = "idx_notification_read", columnList = "user_id, is_read")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "is_read", nullable = false)
    @Builder.Default
    private boolean read = false;

    @Column(name = "link")
    private String link;

    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "entity_id")
    private Long entityId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum NotificationType {
        DEAL_WON,
        DEAL_LOST,
        DEAL_ASSIGNED,
        CONTACT_CREATED,
        ACTIVITY_DUE,
        ACTIVITY_OVERDUE,
        MENTION,
        SYSTEM,
        IMPORT_COMPLETE,
        EMAIL_BOUNCED
    }
}
