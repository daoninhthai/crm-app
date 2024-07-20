package com.daoninhthai.crm.dto;

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
public class ContactGrowthReport {

    private long totalContacts;
    private long newContactsThisMonth;
    private long newContactsLastMonth;
    private double growthPercentage;
    private Map<String, Long> contactsByStatus;
    private List<MonthlyGrowth> monthlyGrowth;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyGrowth {
        private String month;
        private long count;
    }
}
