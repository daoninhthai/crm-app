package com.daoninhthai.crm.dto;

import com.daoninhthai.crm.entity.Activity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateActivityRequest {

    @NotNull(message = "Activity type is required")
    private Activity.ActivityType type;

    @NotBlank(message = "Subject is required")
    private String subject;

    private String description;

    private Long contactId;

    private Long dealId;

    private LocalDateTime dueDate;
}
