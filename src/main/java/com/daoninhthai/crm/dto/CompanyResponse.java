package com.daoninhthai.crm.dto;

import com.daoninhthai.crm.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponse {

    private Long id;
    private String name;
    private String industry;
    private String website;
    private String phone;
    private String address;
    private Company.CompanySize size;
    private int contactCount;
    private int dealCount;
    private LocalDateTime createdAt;
}
