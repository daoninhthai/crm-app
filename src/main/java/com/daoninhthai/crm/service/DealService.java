package com.daoninhthai.crm.service;

import com.daoninhthai.crm.dto.CreateDealRequest;
import com.daoninhthai.crm.dto.DealResponse;
import com.daoninhthai.crm.dto.UpdateDealRequest;
import com.daoninhthai.crm.entity.Company;
import com.daoninhthai.crm.entity.Contact;
import com.daoninhthai.crm.entity.Deal;
import com.daoninhthai.crm.exception.ResourceNotFoundException;
import com.daoninhthai.crm.mapper.DealMapper;
import com.daoninhthai.crm.repository.CompanyRepository;
import com.daoninhthai.crm.repository.ContactRepository;
import com.daoninhthai.crm.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DealService {

    private final DealRepository dealRepository;
    private final CompanyRepository companyRepository;
    private final ContactRepository contactRepository;
    private final DealMapper dealMapper;

    @Transactional
    public DealResponse create(CreateDealRequest request) {
        Deal deal = dealMapper.toEntity(request);

        if (request.getCompanyId() != null) {
            Company company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company", "id", request.getCompanyId()));
            deal.setCompany(company);
        }

        if (request.getContactId() != null) {
            Contact contact = contactRepository.findById(request.getContactId())
                    .orElseThrow(() -> new ResourceNotFoundException("Contact", "id", request.getContactId()));
            deal.setContact(contact);
        }

        Deal saved = dealRepository.save(deal);
        return dealMapper.toResponse(saved);
    }

    @Transactional
    public DealResponse update(Long id, UpdateDealRequest request) {
        Deal deal = dealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deal", "id", id));

        if (request.getTitle() != null) deal.setTitle(request.getTitle());
        if (request.getValue() != null) deal.setValue(request.getValue());
        if (request.getStage() != null) deal.setStage(request.getStage());
        if (request.getExpectedCloseDate() != null) deal.setExpectedCloseDate(request.getExpectedCloseDate());
        if (request.getProbability() != null) deal.setProbability(request.getProbability());

        if (request.getCompanyId() != null) {
            Company company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company", "id", request.getCompanyId()));
            deal.setCompany(company);
        }

        if (request.getContactId() != null) {
            Contact contact = contactRepository.findById(request.getContactId())
                    .orElseThrow(() -> new ResourceNotFoundException("Contact", "id", request.getContactId()));
            deal.setContact(contact);
        }

        Deal updated = dealRepository.save(deal);
        return dealMapper.toResponse(updated);
    }

    @Transactional(readOnly = true)
    public DealResponse getById(Long id) {
        Deal deal = dealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deal", "id", id));
        return dealMapper.toResponse(deal);
    }

    @Transactional(readOnly = true)
    public Page<DealResponse> getAll(Pageable pageable) {
        return dealRepository.findAll(pageable)
                .map(dealMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<DealResponse> getByStage(Deal.DealStage stage) {
        return dealRepository.findByStage(stage).stream()
                .map(dealMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DealResponse> getByCompany(Long companyId) {
        return dealRepository.findByCompanyId(companyId).stream()
                .map(dealMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DealResponse> getByContact(Long contactId) {
        return dealRepository.findByContactId(contactId).stream()
                .map(dealMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        if (!dealRepository.existsById(id)) {
            throw new ResourceNotFoundException("Deal", "id", id);
        }
        dealRepository.deleteById(id);
    }
}
