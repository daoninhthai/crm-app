package com.daoninhthai.crm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailRequest {

    @NotBlank(message = "Recipient email is required")
    @Email(message = "Invalid email address")
    private String to;

    @NotBlank(message = "Subject is required")
    private String subject;

    private String body;

    private String templateId;

    private Map<String, String> variables;

    private Long contactId;

    // For bulk email
    private List<String> recipients;
}
