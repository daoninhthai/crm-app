package com.daoninhthai.crm.service;

import com.daoninhthai.crm.dto.DealResponse;
import com.daoninhthai.crm.dto.MoveDealRequest;
import com.daoninhthai.crm.dto.PipelineSummary;
import com.daoninhthai.crm.entity.Deal;
import com.daoninhthai.crm.exception.ResourceNotFoundException;
import com.daoninhthai.crm.mapper.DealMapper;
import com.daoninhthai.crm.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PipelineService {

    private final DealRepository dealRepository;
    private final DealMapper dealMapper;

    @Transactional(readOnly = true)
    public Map<Deal.DealStage, List<DealResponse>> getDealsByStage() {
        Map<Deal.DealStage, List<DealResponse>> pipeline = new LinkedHashMap<>();

        for (Deal.DealStage stage : Deal.DealStage.values()) {
            List<DealResponse> deals = dealRepository.findByStage(stage).stream()
                    .map(dealMapper::toResponse)
                    .collect(Collectors.toList());
            pipeline.put(stage, deals);
        }

        return pipeline;
    }

    @Transactional
    public DealResponse moveDeal(Long dealId, MoveDealRequest request) {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new ResourceNotFoundException("Deal", "id", dealId));

        deal.setStage(request.getTargetStage());
        Deal updated = dealRepository.save(deal);
        return dealMapper.toResponse(updated);
    }

    @Transactional(readOnly = true)
    public List<PipelineSummary> getPipelineSummary() {
        List<Object[]> results = dealRepository.calculateTotalValueByStage();
        List<PipelineSummary> summaries = new ArrayList<>();

        Set<Deal.DealStage> stagesWithData = new HashSet<>();

        for (Object[] row : results) {
            Deal.DealStage stage = (Deal.DealStage) row[0];
            Long count = (Long) row[1];
            BigDecimal totalValue = (BigDecimal) row[2];

            stagesWithData.add(stage);
            summaries.add(PipelineSummary.builder()
                    .stage(stage)
                    .count(count)
                    .totalValue(totalValue)
                    .build());
        }

        // Add stages with zero deals
        for (Deal.DealStage stage : Deal.DealStage.values()) {
            if (!stagesWithData.contains(stage)) {
                summaries.add(PipelineSummary.builder()
                        .stage(stage)
                        .count(0L)
                        .totalValue(BigDecimal.ZERO)
                        .build());
            }
        }

        summaries.sort(Comparator.comparingInt(s -> s.getStage().ordinal()));
        return summaries;
    }
}
