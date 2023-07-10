package com.daoninhthai.crm.service;

import com.daoninhthai.crm.dto.CompanyResponse;
import com.daoninhthai.crm.dto.CreateCompanyRequest;
import com.daoninhthai.crm.entity.Company;
import com.daoninhthai.crm.exception.ResourceNotFoundException;
import com.daoninhthai.crm.mapper.CompanyMapper;
import com.daoninhthai.crm.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    @Transactional
    public CompanyResponse create(CreateCompanyRequest request) {
        Company company = companyMapper.toEntity(request);
        Company saved = companyRepository.save(company);
        return companyMapper.toResponse(saved);
    }

    @Transactional
    public CompanyResponse update(Long id, CreateCompanyRequest request) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", id));

        company.setName(request.getName());
        company.setIndustry(request.getIndustry());
        company.setWebsite(request.getWebsite());
        company.setPhone(request.getPhone());
        company.setAddress(request.getAddress());
        company.setSize(request.getSize());

        Company updated = companyRepository.save(company);
        return companyMapper.toResponse(updated);
    }

    @Transactional(readOnly = true)
    public CompanyResponse getById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", id));
        return companyMapper.toResponse(company);
    }

    @Transactional(readOnly = true)
    public Page<CompanyResponse> getAll(Pageable pageable) {
        return companyRepository.findAll(pageable)
                .map(companyMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<CompanyResponse> search(String name, Pageable pageable) {
        return companyRepository.searchByName(name, pageable)
                .map(companyMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<CompanyResponse> getByIndustry(String industry) {
        return companyRepository.findByIndustry(industry).stream()
                .map(companyMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Company", "id", id);
        }
        companyRepository.deleteById(id);
    }
}
