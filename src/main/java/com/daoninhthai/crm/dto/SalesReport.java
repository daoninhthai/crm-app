package com.daoninhthai.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesReport {

    private BigDecimal totalRevenue;
    private BigDecimal averageDealValue;
    private long totalDealsWon;
    private long totalDealsLost;
    private BigDecimal winRate;
    private List<StageBreakdown> stageBreakdown;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StageBreakdown {
        private String stage;
        private long count;
        private BigDecimal totalValue;
    }
}
