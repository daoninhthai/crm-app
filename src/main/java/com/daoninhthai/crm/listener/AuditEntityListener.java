package com.daoninhthai.crm.listener;

import com.daoninhthai.crm.entity.AuditLog;
import com.daoninhthai.crm.repository.AuditLogRepository;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * JPA Entity listener that automatically creates audit log entries
 * when entities are created, updated, or deleted.
 *
 * Note: This listener uses static access to Spring context for repository access.
 * For production use, consider using Spring AOP or ApplicationEventPublisher instead.
 */
@Component
@Slf4j
public class AuditEntityListener {

    private static AuditLogRepository auditLogRepository;

    public AuditEntityListener() {
        // Default constructor for JPA
    }

    public AuditEntityListener(AuditLogRepository auditLogRepository) {
        AuditEntityListener.auditLogRepository = auditLogRepository;
    }

    @PostPersist
    public void onPostPersist(Object entity) {
        createAuditLog(entity, AuditLog.AuditAction.CREATE);
    }

    @PostUpdate
    public void onPostUpdate(Object entity) {
        createAuditLog(entity, AuditLog.AuditAction.UPDATE);
    }

    @PostRemove
    public void onPostRemove(Object entity) {
        createAuditLog(entity, AuditLog.AuditAction.DELETE);
    }

    private void createAuditLog(Object entity, AuditLog.AuditAction action) {
        try {
            if (auditLogRepository == null) {
                log.debug("AuditLogRepository not initialized, skipping audit for {}", entity.getClass().getSimpleName());
                return;
            }

            // Don't audit the audit log itself
            if (entity instanceof AuditLog) {
                return;
            }

            String entityType = entity.getClass().getSimpleName();
            Long entityId = extractEntityId(entity);

            AuditLog auditLog = AuditLog.builder()
                    .entityType(entityType)
                    .entityId(entityId != null ? entityId : 0L)
                    .action(action)
                    .newValue(entity.toString())
                    .username(getCurrentUsername())
                    .build();

            auditLogRepository.save(auditLog);
            log.debug("Audit log created: {} {} {}", action, entityType, entityId);
        } catch (Exception e) {
            log.error("Failed to create audit log for entity: {}", entity.getClass().getSimpleName(), e);
        }
    }

    private Long extractEntityId(Object entity) {
        try {
            var method = entity.getClass().getMethod("getId");
            Object id = method.invoke(entity);
            if (id instanceof Long) {
                return (Long) id;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "system";
    }
}
