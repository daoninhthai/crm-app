package com.daoninhthai.crm.controller;

import com.daoninhthai.crm.dto.*;
import com.daoninhthai.crm.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/sales")
    public ResponseEntity<SalesReport> getSalesReport() {
        SalesReport report = reportService.getSalesReport();
        return ResponseEntity.ok(report);
    }

    @GetMapping("/contact-growth")
    public ResponseEntity<ContactGrowthReport> getContactGrowthReport() {
        ContactGrowthReport report = reportService.getContactGrowthReport();
        return ResponseEntity.ok(report);
    }

    @GetMapping("/deal-conversion")
    public ResponseEntity<DealConversionReport> getDealConversionReport() {
        DealConversionReport report = reportService.getDealConversionReport();
        return ResponseEntity.ok(report);
    }

    @GetMapping("/activity-summary")
    public ResponseEntity<ActivityReport> getActivityReport() {
        ActivityReport report = reportService.getActivityReport();
        return ResponseEntity.ok(report);
    }

    @GetMapping("/revenue-monthly")
    public ResponseEntity<Map<String, BigDecimal>> getRevenueByMonth() {
        Map<String, BigDecimal> revenue = reportService.getRevenueByMonth();
        return ResponseEntity.ok(revenue);
    }
}
