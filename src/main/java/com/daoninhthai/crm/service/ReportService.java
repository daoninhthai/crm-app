package com.daoninhthai.crm.service;

import com.daoninhthai.crm.dto.*;
import com.daoninhthai.crm.entity.Activity;
import com.daoninhthai.crm.entity.Contact;
import com.daoninhthai.crm.entity.Deal;
import com.daoninhthai.crm.repository.ActivityRepository;
import com.daoninhthai.crm.repository.ContactRepository;
import com.daoninhthai.crm.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final DealRepository dealRepository;
    private final ContactRepository contactRepository;
    private final ActivityRepository activityRepository;

    @Transactional(readOnly = true)
    public SalesReport getSalesReport() {
        List<Object[]> stageData = dealRepository.calculateTotalValueByStage();
        BigDecimal totalRevenue = dealRepository.getTotalRevenue();
        long totalDeals = dealRepository.count();
        long wonDeals = dealRepository.countByStage(Deal.DealStage.CLOSED_WON);
        long lostDeals = dealRepository.countByStage(Deal.DealStage.CLOSED_LOST);

        BigDecimal avgDealValue = totalDeals > 0
                ? totalRevenue.divide(BigDecimal.valueOf(Math.max(wonDeals, 1)), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        BigDecimal winRate = totalDeals > 0
                ? BigDecimal.valueOf(wonDeals).multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(totalDeals), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        List<SalesReport.StageBreakdown> breakdown = new ArrayList<>();
        for (Object[] row : stageData) {
            breakdown.add(SalesReport.StageBreakdown.builder()
                    .stage(row[0].toString())
                    .count((Long) row[1])
                    .totalValue((BigDecimal) row[2])
                    .build());
        }

        return SalesReport.builder()
                .totalRevenue(totalRevenue != null ? totalRevenue : BigDecimal.ZERO)
                .averageDealValue(avgDealValue)
                .totalDealsWon(wonDeals)
                .totalDealsLost(lostDeals)
                .winRate(winRate)
                .stageBreakdown(breakdown)
                .build();
    }

    @Transactional(readOnly = true)
    public ContactGrowthReport getContactGrowthReport() {
        long totalContacts = contactRepository.count();

        Map<String, Long> byStatus = new LinkedHashMap<>();
        for (Contact.ContactStatus status : Contact.ContactStatus.values()) {
            byStatus.put(status.name(), contactRepository.countByStatus(status));
        }

        return ContactGrowthReport.builder()
                .totalContacts(totalContacts)
                .newContactsThisMonth(0L)
                .newContactsLastMonth(0L)
                .growthPercentage(0.0)
                .contactsByStatus(byStatus)
                .monthlyGrowth(new ArrayList<>())
                .build();
    }

    @Transactional(readOnly = true)
    public DealConversionReport getDealConversionReport() {
        long totalDeals = dealRepository.count();
        long wonDeals = dealRepository.countByStage(Deal.DealStage.CLOSED_WON);

        BigDecimal conversionRate = totalDeals > 0
                ? BigDecimal.valueOf(wonDeals).multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(totalDeals), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        List<DealConversionReport.StageConversion> stageConversions = new ArrayList<>();
        Deal.DealStage[] stages = Deal.DealStage.values();
        for (int i = 0; i < stages.length - 1; i++) {
            long fromCount = dealRepository.countByStage(stages[i]);
            long toCount = dealRepository.countByStage(stages[i + 1]);
            BigDecimal rate = fromCount > 0
                    ? BigDecimal.valueOf(toCount).multiply(BigDecimal.valueOf(100))
                        .divide(BigDecimal.valueOf(fromCount), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            stageConversions.add(DealConversionReport.StageConversion.builder()
                    .fromStage(stages[i].name())
                    .toStage(stages[i + 1].name())
                    .count(toCount)
                    .conversionRate(rate)
                    .build());
        }

        return DealConversionReport.builder()
                .overallConversionRate(conversionRate)
                .totalDeals(totalDeals)
                .convertedDeals(wonDeals)
                .averageTimeToClose(BigDecimal.ZERO)
                .stageConversions(stageConversions)
                .build();
    }

    @Transactional(readOnly = true)
    public ActivityReport getActivityReport() {
        long totalActivities = activityRepository.count();
        List<Activity> overdue = activityRepository.findOverdue(LocalDateTime.now());

        Map<String, Long> byType = new LinkedHashMap<>();
        for (Activity.ActivityType type : Activity.ActivityType.values()) {
            long count = activityRepository.findByType(type, org.springframework.data.domain.Pageable.unpaged())
                    .getTotalElements();
            byType.put(type.name(), count);
        }

        return ActivityReport.builder()
                .totalActivities(totalActivities)
                .completedActivities(0L)
                .overdueActivities(overdue.size())
                .completionRate(0.0)
                .activitiesByType(byType)
                .dailyActivities(new ArrayList<>())
                .build();
    }

    @Transactional(readOnly = true)
    public Map<String, BigDecimal> getRevenueByMonth() {
        Map<String, BigDecimal> revenueByMonth = new LinkedHashMap<>();
        // Return structure for frontend chart consumption
        // In production, this would query deals by closed_at month
        BigDecimal totalRevenue = dealRepository.getTotalRevenue();
        revenueByMonth.put("total", totalRevenue != null ? totalRevenue : BigDecimal.ZERO);

        return revenueByMonth;
    }
}
