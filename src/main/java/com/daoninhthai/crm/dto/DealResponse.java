package com.daoninhthai.crm.dto;

import com.daoninhthai.crm.entity.Deal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DealResponse {

    private Long id;
    private String title;
    private BigDecimal value;
    private Deal.DealStage stage;
    private Long companyId;
    private String companyName;
    private Long contactId;
    private String contactName;
    private LocalDate expectedCloseDate;
    private BigDecimal probability;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
