package com.daoninhthai.crm.service;

import com.daoninhthai.crm.dto.DashboardStats;
import com.daoninhthai.crm.dto.DealResponse;
import com.daoninhthai.crm.entity.Activity;
import com.daoninhthai.crm.entity.Deal;
import com.daoninhthai.crm.mapper.DealMapper;
import com.daoninhthai.crm.repository.ActivityRepository;
import com.daoninhthai.crm.repository.ContactRepository;
import com.daoninhthai.crm.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DealRepository dealRepository;
    private final ContactRepository contactRepository;
    private final ActivityRepository activityRepository;
    private final DealMapper dealMapper;

    @Transactional(readOnly = true)
    public BigDecimal getTotalRevenue() {
        BigDecimal revenue = dealRepository.getTotalRevenue();
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    public BigDecimal getDealConversionRate() {
        long totalDeals = dealRepository.count();
        if (totalDeals == 0) {
            return BigDecimal.ZERO;
        }

        long closedWon = dealRepository.countByStage(Deal.DealStage.CLOSED_WON);
        return BigDecimal.valueOf(closedWon)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(totalDeals), 2, RoundingMode.HALF_UP);
    }

    @Transactional(readOnly = true)
    public List<DealResponse> getTopDeals(int limit) {
        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "value"));
        return dealRepository.findAll(pageRequest).getContent().stream()
                .map(dealMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Activity> getRecentActivities() {
        return activityRepository.findTop10ByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public DashboardStats getDashboardStats() {
        long totalContacts = contactRepository.count();
        long activeDeals = dealRepository.count() - dealRepository.countByStage(Deal.DealStage.CLOSED_WON)
                - dealRepository.countByStage(Deal.DealStage.CLOSED_LOST);
        BigDecimal revenue = getTotalRevenue();
        BigDecimal conversionRate = getDealConversionRate();

        return DashboardStats.builder()
                .totalContacts(totalContacts)
                .activeDeals(activeDeals)
                .revenue(revenue)
                .conversionRate(conversionRate)
                .build();
    }
}
