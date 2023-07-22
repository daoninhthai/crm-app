package com.daoninhthai.crm.dto;

import com.daoninhthai.crm.entity.Deal;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveDealRequest {

    @NotNull(message = "Target stage is required")
    private Deal.DealStage targetStage;
}
