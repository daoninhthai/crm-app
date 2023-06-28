package com.daoninhthai.crm.service;

import com.daoninhthai.crm.dto.ContactResponse;
import com.daoninhthai.crm.dto.CreateContactRequest;
import com.daoninhthai.crm.dto.UpdateContactRequest;
import com.daoninhthai.crm.entity.Company;
import com.daoninhthai.crm.entity.Contact;
import com.daoninhthai.crm.exception.ResourceNotFoundException;
import com.daoninhthai.crm.mapper.ContactMapper;
import com.daoninhthai.crm.repository.CompanyRepository;
import com.daoninhthai.crm.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;
    private final CompanyRepository companyRepository;
    private final ContactMapper contactMapper;

    @Transactional
    public ContactResponse create(CreateContactRequest request) {
        Contact contact = contactMapper.toEntity(request);

        if (request.getCompanyId() != null) {
            Company company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company", "id", request.getCompanyId()));
            contact.setCompany(company);
        }

        Contact saved = contactRepository.save(contact);
        return contactMapper.toResponse(saved);
    }

    @Transactional
    public ContactResponse update(Long id, UpdateContactRequest request) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact", "id", id));

        if (request.getFirstName() != null) contact.setFirstName(request.getFirstName());
        if (request.getLastName() != null) contact.setLastName(request.getLastName());
        if (request.getEmail() != null) contact.setEmail(request.getEmail());
        if (request.getPhone() != null) contact.setPhone(request.getPhone());
        if (request.getPosition() != null) contact.setPosition(request.getPosition());
        if (request.getNotes() != null) contact.setNotes(request.getNotes());
        if (request.getStatus() != null) contact.setStatus(request.getStatus());

        if (request.getCompanyId() != null) {
            Company company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company", "id", request.getCompanyId()));
            contact.setCompany(company);
        }

        Contact updated = contactRepository.save(contact);
        return contactMapper.toResponse(updated);
    }

    @Transactional(readOnly = true)
    public ContactResponse getById(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact", "id", id));
        return contactMapper.toResponse(contact);
    }

    @Transactional(readOnly = true)
    public Page<ContactResponse> getAll(Pageable pageable) {
        return contactRepository.findAll(pageable)
                .map(contactMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ContactResponse> search(String keyword, Pageable pageable) {
        return contactRepository.searchByNameOrEmail(keyword, pageable)
                .map(contactMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<ContactResponse> getByCompany(Long companyId) {
        return contactRepository.findByCompanyId(companyId).stream()
                .map(contactMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        if (!contactRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contact", "id", id);
        }
        contactRepository.deleteById(id);
    }
}
