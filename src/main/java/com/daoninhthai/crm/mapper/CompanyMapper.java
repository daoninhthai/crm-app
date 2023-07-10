package com.daoninhthai.crm.mapper;

import com.daoninhthai.crm.dto.CompanyResponse;
import com.daoninhthai.crm.dto.CreateCompanyRequest;
import com.daoninhthai.crm.entity.Company;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    public CompanyResponse toResponse(Company company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .industry(company.getIndustry())
                .website(company.getWebsite())
                .phone(company.getPhone())
                .address(company.getAddress())
                .size(company.getSize())
                .contactCount(company.getContacts() != null ? company.getContacts().size() : 0)
                .dealCount(company.getDeals() != null ? company.getDeals().size() : 0)
                .createdAt(company.getCreatedAt())
                .build();
    }

    public Company toEntity(CreateCompanyRequest request) {
        return Company.builder()
                .name(request.getName())
                .industry(request.getIndustry())
                .website(request.getWebsite())
                .phone(request.getPhone())
                .address(request.getAddress())
                .size(request.getSize())
                .build();
    }
}
