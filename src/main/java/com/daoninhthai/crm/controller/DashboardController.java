package com.daoninhthai.crm.controller;

import com.daoninhthai.crm.dto.DashboardStats;
import com.daoninhthai.crm.dto.DealResponse;
import com.daoninhthai.crm.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStats> getStats() {
        DashboardStats stats = dashboardService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/revenue")
    public ResponseEntity<BigDecimal> getRevenue() {
        BigDecimal revenue = dashboardService.getTotalRevenue();
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/top-deals")
    public ResponseEntity<List<DealResponse>> getTopDeals(
            @RequestParam(defaultValue = "10") int limit) {
        List<DealResponse> topDeals = dashboardService.getTopDeals(limit);
        return ResponseEntity.ok(topDeals);
    }
}
