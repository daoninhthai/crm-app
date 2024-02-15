package com.daoninhthai.crm.service;

import com.daoninhthai.crm.config.EmailConfig;
import com.daoninhthai.crm.dto.SendEmailRequest;
import com.daoninhthai.crm.entity.EmailLog;
import com.daoninhthai.crm.repository.EmailLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final WebClient sendGridWebClient;
    private final EmailConfig emailConfig;
    private final EmailLogRepository emailLogRepository;

    @Transactional
    public EmailLog sendEmail(SendEmailRequest request) {
        EmailLog emailLog = EmailLog.builder()
                .to(request.getTo())
                .subject(request.getSubject())
                .body(request.getBody())
                .contactId(request.getContactId())
                .templateId(request.getTemplateId())
                .build();

        if (!emailConfig.isEnabled()) {
            log.info("Email sending disabled. Would send to: {}", request.getTo());
            emailLog.setStatus(EmailLog.EmailStatus.SENT);
            emailLog.setSentAt(LocalDateTime.now());
            return emailLogRepository.save(emailLog);
        }

        try {
            Map<String, Object> payload = buildSendGridPayload(request);

            sendGridWebClient.post()
                    .uri("/mail/send")
                    .bodyValue(payload)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            emailLog.setStatus(EmailLog.EmailStatus.SENT);
            emailLog.setSentAt(LocalDateTime.now());
            log.info("Email sent successfully to: {}", request.getTo());
        } catch (Exception e) {
            log.error("Failed to send email to: {}", request.getTo(), e);
            emailLog.setStatus(EmailLog.EmailStatus.FAILED);
            emailLog.setErrorMessage(e.getMessage());
        }

        return emailLogRepository.save(emailLog);
    }

    @Transactional
    public List<EmailLog> sendBulkEmail(SendEmailRequest request) {
        List<EmailLog> results = new ArrayList<>();

        if (request.getRecipients() == null || request.getRecipients().isEmpty()) {
            return results;
        }

        for (String recipient : request.getRecipients()) {
            SendEmailRequest singleRequest = SendEmailRequest.builder()
                    .to(recipient)
                    .subject(request.getSubject())
                    .body(request.getBody())
                    .templateId(request.getTemplateId())
                    .variables(request.getVariables())
                    .build();

            EmailLog result = sendEmail(singleRequest);
            results.add(result);
        }

        return results;
    }

    @Transactional
    public EmailLog sendTemplateEmail(SendEmailRequest request) {
        if (request.getTemplateId() == null || request.getTemplateId().isBlank()) {
            throw new IllegalArgumentException("Template ID is required for template emails");
        }

        String processedBody = processTemplate(request.getBody(), request.getVariables());
        request.setBody(processedBody);

        return sendEmail(request);
    }

    @Transactional(readOnly = true)
    public List<EmailLog> getEmailsByContact(Long contactId) {
        return emailLogRepository.findByContactId(contactId);
    }

    @Transactional(readOnly = true)
    public List<EmailLog> getEmailsByRecipient(String email) {
        return emailLogRepository.findByTo(email);
    }

    private Map<String, Object> buildSendGridPayload(SendEmailRequest request) {
        Map<String, Object> payload = new HashMap<>();

        // Personalizations
        List<Map<String, Object>> personalizations = new ArrayList<>();
        Map<String, Object> personalization = new HashMap<>();
        List<Map<String, String>> toList = new ArrayList<>();
        Map<String, String> toEntry = new HashMap<>();
        toEntry.put("email", request.getTo());
        toList.add(toEntry);
        personalization.put("to", toList);

        if (request.getVariables() != null && !request.getVariables().isEmpty()) {
            personalization.put("dynamic_template_data", request.getVariables());
        }
        personalizations.add(personalization);
        payload.put("personalizations", personalizations);

        // From
        Map<String, String> from = new HashMap<>();
        from.put("email", emailConfig.getFromAddress());
        from.put("name", emailConfig.getFromName());
        payload.put("from", from);

        // Subject
        payload.put("subject", request.getSubject());

        // Content
        if (request.getBody() != null) {
            List<Map<String, String>> content = new ArrayList<>();
            Map<String, String> contentEntry = new HashMap<>();
            contentEntry.put("type", "text/html");
            contentEntry.put("value", request.getBody());
            content.add(contentEntry);
            payload.put("content", content);
        }

        // Template ID
        if (request.getTemplateId() != null) {
            payload.put("template_id", request.getTemplateId());
        }

        return payload;
    }

    private String processTemplate(String template, Map<String, String> variables) {
        if (template == null || variables == null) {
            return template;
        }

        String result = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            result = result.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return result;
    }
}
