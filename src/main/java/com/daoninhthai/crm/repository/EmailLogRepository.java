package com.daoninhthai.crm.repository;

import com.daoninhthai.crm.entity.EmailLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {

    List<EmailLog> findByContactId(Long contactId);

    Page<EmailLog> findByStatus(EmailLog.EmailStatus status, Pageable pageable);

    List<EmailLog> findBySentAtBetween(LocalDateTime from, LocalDateTime to);

    long countByStatus(EmailLog.EmailStatus status);

    List<EmailLog> findByTo(String recipientEmail);
}
