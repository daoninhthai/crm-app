package com.daoninhthai.crm.dto;

import com.daoninhthai.crm.entity.Contact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkUpdateStatusRequest {
    private List<Long> ids;
    private Contact.ContactStatus status;
}
