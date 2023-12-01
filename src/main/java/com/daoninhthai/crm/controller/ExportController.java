package com.daoninhthai.crm.controller;

import com.daoninhthai.crm.service.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
public class ExportController {

    private final ExportService exportService;

    @GetMapping(value = "/contacts", produces = "text/csv")
    public ResponseEntity<String> exportContacts() {
        String csv = exportService.exportContactsToCSV();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contacts.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(csv);
    }

    @GetMapping(value = "/deals", produces = "text/csv")
    public ResponseEntity<String> exportDeals() {
        String csv = exportService.exportDealsToCSV();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=deals.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(csv);
    }
}
