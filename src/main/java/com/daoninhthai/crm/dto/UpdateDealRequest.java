package com.daoninhthai.crm.dto;

import com.daoninhthai.crm.entity.Deal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDealRequest {

    private String title;

    private BigDecimal value;

    private Deal.DealStage stage;

    private Long companyId;

    private Long contactId;

    private LocalDate expectedCloseDate;

    private BigDecimal probability;
}
