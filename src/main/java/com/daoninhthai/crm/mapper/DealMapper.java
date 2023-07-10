package com.daoninhthai.crm.mapper;

import com.daoninhthai.crm.dto.CreateDealRequest;
import com.daoninhthai.crm.dto.DealResponse;
import com.daoninhthai.crm.entity.Deal;
import org.springframework.stereotype.Component;

@Component
public class DealMapper {

    public DealResponse toResponse(Deal deal) {
        return DealResponse.builder()
                .id(deal.getId())
                .title(deal.getTitle())
                .value(deal.getValue())
                .stage(deal.getStage())
                .companyId(deal.getCompany() != null ? deal.getCompany().getId() : null)
                .companyName(deal.getCompany() != null ? deal.getCompany().getName() : null)
                .contactId(deal.getContact() != null ? deal.getContact().getId() : null)
                .contactName(deal.getContact() != null ? deal.getContact().getFullName() : null)
                .expectedCloseDate(deal.getExpectedCloseDate())
                .probability(deal.getProbability())
                .createdAt(deal.getCreatedAt())
                .updatedAt(deal.getUpdatedAt())
                .build();
    }

    public Deal toEntity(CreateDealRequest request) {
        return Deal.builder()
                .title(request.getTitle())
                .value(request.getValue())
                .stage(request.getStage() != null ? request.getStage() : Deal.DealStage.LEAD)
                .expectedCloseDate(request.getExpectedCloseDate())
                .probability(request.getProbability())
                .build();
    }
}
