package com.daoninhthai.crm.dto;

import com.daoninhthai.crm.entity.Activity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityResponse {

    private Long id;
    private Activity.ActivityType type;
    private String subject;
    private String description;
    private Long contactId;
    private String contactName;
    private Long dealId;
    private String dealTitle;
    private LocalDateTime dueDate;
    private boolean completed;
    private String createdBy;
    private LocalDateTime createdAt;
}
