package com.daoninhthai.crm.dto;

import com.daoninhthai.crm.entity.Deal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineSummary {

    private Deal.DealStage stage;
    private Long count;
    private BigDecimal totalValue;
}
