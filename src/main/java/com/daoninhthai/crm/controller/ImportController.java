package com.daoninhthai.crm.controller;

import com.daoninhthai.crm.dto.ImportResult;
import com.daoninhthai.crm.service.ImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class ImportController {

    private final ImportService importService;

    @PostMapping("/contacts")
    public ResponseEntity<ImportResult> importContacts(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            String contentType = file.getContentType();
            String filename = file.getOriginalFilename();
            if (filename == null || (!filename.endsWith(".csv") && !"text/csv".equals(contentType))) {
                return ResponseEntity.badRequest().build();
            }

            ImportResult result = importService.importContactsFromCsv(file.getInputStream());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/companies")
    public ResponseEntity<ImportResult> importCompanies(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            String contentType = file.getContentType();
            String filename = file.getOriginalFilename();
            if (filename == null || (!filename.endsWith(".csv") && !"text/csv".equals(contentType))) {
                return ResponseEntity.badRequest().build();
            }

            ImportResult result = importService.importCompaniesFromCsv(file.getInputStream());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
