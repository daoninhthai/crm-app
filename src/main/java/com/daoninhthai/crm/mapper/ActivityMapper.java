package com.daoninhthai.crm.mapper;

import com.daoninhthai.crm.dto.ActivityResponse;
import com.daoninhthai.crm.dto.CreateActivityRequest;
import com.daoninhthai.crm.entity.Activity;
import org.springframework.stereotype.Component;

@Component
public class ActivityMapper {

    public ActivityResponse toResponse(Activity activity) {
        return ActivityResponse.builder()
                .id(activity.getId())
                .type(activity.getType())
                .subject(activity.getSubject())
                .description(activity.getDescription())
                .contactId(activity.getContact() != null ? activity.getContact().getId() : null)
                .contactName(activity.getContact() != null ? activity.getContact().getFullName() : null)
                .dealId(activity.getDeal() != null ? activity.getDeal().getId() : null)
                .dealTitle(activity.getDeal() != null ? activity.getDeal().getTitle() : null)
                .dueDate(activity.getDueDate())
                .completed(activity.isCompleted())
                .createdBy(activity.getCreatedBy())
                .createdAt(activity.getCreatedAt())
                .build();
    }

    public Activity toEntity(CreateActivityRequest request) {
        return Activity.builder()
                .type(request.getType())
                .subject(request.getSubject())
                .description(request.getDescription())
                .dueDate(request.getDueDate())
                .completed(false)
                .build();
    }
}
