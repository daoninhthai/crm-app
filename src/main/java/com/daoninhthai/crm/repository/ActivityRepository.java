package com.daoninhthai.crm.repository;

import com.daoninhthai.crm.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findByContactId(Long contactId);

    List<Activity> findByDealId(Long dealId);

    Page<Activity> findByType(Activity.ActivityType type, Pageable pageable);

    @Query("SELECT a FROM Activity a WHERE a.dueDate > :now AND a.completed = false " +
           "ORDER BY a.dueDate ASC")
    List<Activity> findUpcoming(@Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT a FROM Activity a WHERE a.dueDate < :now AND a.completed = false " +
           "ORDER BY a.dueDate ASC")
    List<Activity> findOverdue(@Param("now") LocalDateTime now);

    List<Activity> findTop10ByOrderByCreatedAtDesc();
}
