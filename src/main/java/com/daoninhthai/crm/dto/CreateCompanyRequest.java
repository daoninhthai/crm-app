package com.daoninhthai.crm.dto;

import com.daoninhthai.crm.entity.Company;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCompanyRequest {

    @NotBlank(message = "Company name is required")
    private String name;

    private String industry;

    private String website;

    private String phone;

    private String address;

    private Company.CompanySize size;
}
