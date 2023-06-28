package com.daoninhthai.crm.dto;

import com.daoninhthai.crm.entity.Contact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phone;
    private Long companyId;
    private String companyName;
    private String position;
    private String notes;
    private Contact.ContactStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
