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
public class DealConversionReport {

    private BigDecimal overallConversionRate;
    private long totalDeals;
    private long convertedDeals;
    private BigDecimal averageTimeToClose;
    private List<StageConversion> stageConversions;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StageConversion {
        private String fromStage;
        private String toStage;
        private long count;
        private BigDecimal conversionRate;
    }
}
