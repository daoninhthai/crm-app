package com.daoninhthai.crm.controller;

import com.daoninhthai.crm.dto.SendEmailRequest;
import com.daoninhthai.crm.entity.EmailLog;
import com.daoninhthai.crm.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<EmailLog> sendEmail(@Valid @RequestBody SendEmailRequest request) {
        EmailLog result = emailService.sendEmail(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/send-bulk")
    public ResponseEntity<List<EmailLog>> sendBulkEmail(@RequestBody SendEmailRequest request) {
        List<EmailLog> results = emailService.sendBulkEmail(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(results);
    }

    @PostMapping("/send-template")
    public ResponseEntity<EmailLog> sendTemplateEmail(@Valid @RequestBody SendEmailRequest request) {
        EmailLog result = emailService.sendTemplateEmail(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/contact/{contactId}")
    public ResponseEntity<List<EmailLog>> getByContact(@PathVariable Long contactId) {
        List<EmailLog> emails = emailService.getEmailsByContact(contactId);
        return ResponseEntity.ok(emails);
    }

    @GetMapping("/recipient")
    public ResponseEntity<List<EmailLog>> getByRecipient(@RequestParam String email) {
        List<EmailLog> emails = emailService.getEmailsByRecipient(email);
        return ResponseEntity.ok(emails);
    }
}
