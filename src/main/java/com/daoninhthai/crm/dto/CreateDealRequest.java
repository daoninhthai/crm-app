package com.daoninhthai.crm.dto;

import com.daoninhthai.crm.entity.Deal;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDealRequest {

    @NotBlank(message = "Deal title is required")
    private String title;

    private BigDecimal value;

    private Deal.DealStage stage = Deal.DealStage.LEAD;

    private Long companyId;

    private Long contactId;

    private LocalDate expectedCloseDate;

    private BigDecimal probability;
}
