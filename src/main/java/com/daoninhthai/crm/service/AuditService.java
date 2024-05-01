package com.daoninhthai.crm.service;

import com.daoninhthai.crm.entity.AuditLog;
import com.daoninhthai.crm.repository.AuditLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public AuditLog logCreate(String entityType, Long entityId, Object newEntity) {
        AuditLog auditLog = AuditLog.builder()
                .entityType(entityType)
                .entityId(entityId)
                .action(AuditLog.AuditAction.CREATE)
                .newValue(toJson(newEntity))
                .username(getCurrentUsername())
                .build();

        return auditLogRepository.save(auditLog);
    }

    @Transactional
    public AuditLog logUpdate(String entityType, Long entityId, Object oldEntity, Object newEntity) {
        AuditLog auditLog = AuditLog.builder()
                .entityType(entityType)
                .entityId(entityId)
                .action(AuditLog.AuditAction.UPDATE)
                .oldValue(toJson(oldEntity))
                .newValue(toJson(newEntity))
                .username(getCurrentUsername())
                .build();

        return auditLogRepository.save(auditLog);
    }

    @Transactional
    public AuditLog logDelete(String entityType, Long entityId, Object deletedEntity) {
        AuditLog auditLog = AuditLog.builder()
                .entityType(entityType)
                .entityId(entityId)
                .action(AuditLog.AuditAction.DELETE)
                .oldValue(toJson(deletedEntity))
                .username(getCurrentUsername())
                .build();

        return auditLogRepository.save(auditLog);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> getAuditTrail(String entityType, Long entityId) {
        return auditLogRepository.findByEntityTypeAndEntityIdOrderByTimestampDesc(entityType, entityId);
    }

    @Transactional(readOnly = true)
    public Page<AuditLog> getByUser(Long userId, Pageable pageable) {
        return auditLogRepository.findByUserId(userId, pageable);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> getByDateRange(LocalDateTime from, LocalDateTime to) {
        return auditLogRepository.findByDateRange(from, to);
    }

    @Transactional(readOnly = true)
    public Page<AuditLog> getByEntityType(String entityType, Pageable pageable) {
        return auditLogRepository.findByEntityType(entityType, pageable);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> getByEntityTypeAndDateRange(String entityType, LocalDateTime from, LocalDateTime to) {
        return auditLogRepository.findByEntityTypeAndDateRange(entityType, from, to);
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "system";
    }

    private String toJson(Object obj) {
        if (obj == null) return null;
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize object to JSON: {}", e.getMessage());
            return obj.toString();
        }
    }
}
