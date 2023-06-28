package com.daoninhthai.crm.dto;

import com.daoninhthai.crm.entity.Contact;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateContactRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Invalid email format")
    private String email;

    private String phone;

    private Long companyId;

    private String position;

    private String notes;

    private Contact.ContactStatus status = Contact.ContactStatus.LEAD;
}
