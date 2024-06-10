package com.daoninhthai.crm.repository;

import com.daoninhthai.crm.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    List<Notification> findByUserIdAndReadFalseOrderByCreatedAtDesc(Long userId);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.userId = :userId AND n.read = false")
    long countUnreadByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Notification n SET n.read = true WHERE n.userId = :userId AND n.read = false")
    int markAllAsRead(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM Notification n WHERE n.createdAt < :cutoffDate")
    int deleteOlderThan(@Param("cutoffDate") LocalDateTime cutoffDate);

    List<Notification> findByUserIdAndType(Long userId, Notification.NotificationType type);
}
