package com.daoninhthai.crm.mapper;

import com.daoninhthai.crm.dto.ContactResponse;
import com.daoninhthai.crm.dto.CreateContactRequest;
import com.daoninhthai.crm.entity.Contact;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {

    public ContactResponse toResponse(Contact contact) {
        return ContactResponse.builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .fullName(contact.getFullName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .companyId(contact.getCompany() != null ? contact.getCompany().getId() : null)
                .companyName(contact.getCompany() != null ? contact.getCompany().getName() : null)
                .position(contact.getPosition())
                .notes(contact.getNotes())
                .status(contact.getStatus())
                .createdAt(contact.getCreatedAt())
                .updatedAt(contact.getUpdatedAt())
                .build();
    }

    public Contact toEntity(CreateContactRequest request) {
        return Contact.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .position(request.getPosition())
                .notes(request.getNotes())
                .status(request.getStatus() != null ? request.getStatus() : Contact.ContactStatus.LEAD)
                .build();
    }
}
