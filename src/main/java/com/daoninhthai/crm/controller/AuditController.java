package com.daoninhthai.crm.controller;

import com.daoninhthai.crm.entity.AuditLog;
import com.daoninhthai.crm.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @GetMapping
    public ResponseEntity<?> getAuditLogs(
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) Long entityId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @PageableDefault(size = 20, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {

        // If entityType and entityId are both provided, get specific audit trail
        if (entityType != null && entityId != null) {
            List<AuditLog> trail = auditService.getAuditTrail(entityType, entityId);
            return ResponseEntity.ok(trail);
        }

        // If userId is provided, filter by user
        if (userId != null) {
            Page<AuditLog> logs = auditService.getByUser(userId, pageable);
            return ResponseEntity.ok(logs);
        }

        // If date range is provided
        if (from != null && to != null) {
            if (entityType != null) {
                List<AuditLog> logs = auditService.getByEntityTypeAndDateRange(entityType, from, to);
                return ResponseEntity.ok(logs);
            }
            List<AuditLog> logs = auditService.getByDateRange(from, to);
            return ResponseEntity.ok(logs);
        }

        // If only entityType is provided
        if (entityType != null) {
            Page<AuditLog> logs = auditService.getByEntityType(entityType, pageable);
            return ResponseEntity.ok(logs);
        }

        // Return all audit logs paginated
        Page<AuditLog> logs = auditService.getByEntityType("Contact", pageable);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/{entityType}/{entityId}")
    public ResponseEntity<List<AuditLog>> getEntityAuditTrail(
            @PathVariable String entityType,
            @PathVariable Long entityId) {
        List<AuditLog> trail = auditService.getAuditTrail(entityType, entityId);
        return ResponseEntity.ok(trail);
    }
}
