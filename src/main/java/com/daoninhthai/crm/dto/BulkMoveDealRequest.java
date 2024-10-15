package com.daoninhthai.crm.dto;

import com.daoninhthai.crm.entity.Deal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkMoveDealRequest {
    private List<Long> ids;
    private Deal.DealStage targetStage;
}
