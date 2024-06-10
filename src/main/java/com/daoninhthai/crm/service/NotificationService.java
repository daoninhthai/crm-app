package com.daoninhthai.crm.service;

import com.daoninhthai.crm.entity.Notification;
import com.daoninhthai.crm.exception.ResourceNotFoundException;
import com.daoninhthai.crm.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public Notification create(Long userId, Notification.NotificationType type,
                                String title, String message, String link) {
        Notification notification = Notification.builder()
                .userId(userId)
                .type(type)
                .title(title)
                .message(message)
                .link(link)
                .build();

        Notification saved = notificationRepository.save(notification);
        log.info("Notification created for user {}: {}", userId, title);

        return saved;
    }

    @Transactional
    public Notification create(Notification notification) {
        Notification saved = notificationRepository.save(notification);
        log.info("Notification created for user {}: {}", notification.getUserId(), notification.getTitle());
        return saved;
    }

    @Transactional
    public Notification markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", notificationId));

        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    @Transactional
    public int markAllAsRead(Long userId) {
        int count = notificationRepository.markAllAsRead(userId);
        log.info("Marked {} notifications as read for user {}", count, userId);
        return count;
    }

    @Transactional(readOnly = true)
    public List<Notification> getUnread(Long userId) {
        return notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
    }

    @Transactional(readOnly = true)
    public Page<Notification> getAllForUser(Long userId, Pageable pageable) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(Long userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }

    @Transactional
    public int deleteOld(int daysOld) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(daysOld);
        int count = notificationRepository.deleteOlderThan(cutoff);
        log.info("Deleted {} old notifications (older than {} days)", count, daysOld);
        return count;
    }

    /**
     * Convenience method to create a deal-related notification.
     */
    @Transactional
    public void notifyDealWon(Long userId, String dealTitle, Long dealId) {
        create(userId, Notification.NotificationType.DEAL_WON,
                "Deal Won: " + dealTitle,
                "Congratulations! The deal '" + dealTitle + "' has been marked as won.",
                "/deals/" + dealId);
    }

    /**
     * Convenience method to create an activity due notification.
     */
    @Transactional
    public void notifyActivityDue(Long userId, String activitySubject, Long activityId) {
        create(userId, Notification.NotificationType.ACTIVITY_DUE,
                "Activity Due: " + activitySubject,
                "The activity '" + activitySubject + "' is due soon.",
                "/activities/" + activityId);
    }
}
