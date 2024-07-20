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
public class ActivityReport {

    private long totalActivities;
    private long completedActivities;
    private long overdueActivities;
    private double completionRate;
    private Map<String, Long> activitiesByType;
    private List<DailyActivity> dailyActivities;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyActivity {
        private String date;
        private long count;
        private long completed;
    }
}
