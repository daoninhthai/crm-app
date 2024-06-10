package com.daoninhthai.crm.controller;

import com.daoninhthai.crm.entity.Notification;
import com.daoninhthai.crm.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<Page<Notification>> getAll(
            @RequestParam Long userId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<Notification> notifications = notificationService.getAllForUser(userId, pageable);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnread(@RequestParam Long userId) {
        List<Notification> unread = notificationService.getUnread(userId);
        return ResponseEntity.ok(unread);
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(@RequestParam Long userId) {
        long count = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable Long id) {
        Notification notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(notification);
    }

    @PutMapping("/read-all")
    public ResponseEntity<Map<String, Integer>> markAllAsRead(@RequestParam Long userId) {
        int count = notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(Map.of("markedAsRead", count));
    }

    @DeleteMapping("/cleanup")
    public ResponseEntity<Map<String, Integer>> deleteOldNotifications(
            @RequestParam(defaultValue = "90") int daysOld) {
        int deleted = notificationService.deleteOld(daysOld);
        return ResponseEntity.ok(Map.of("deleted", deleted));
    }
}
