package com.daoninhthai.crm.dto;

import com.daoninhthai.crm.entity.Contact;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateContactRequest {

    private String firstName;

    private String lastName;

    @Email(message = "Invalid email format")
    private String email;

    private String phone;

    private Long companyId;

    private String position;

    private String notes;

    private Contact.ContactStatus status;
}
