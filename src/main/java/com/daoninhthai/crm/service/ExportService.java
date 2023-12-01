package com.daoninhthai.crm.service;

import com.daoninhthai.crm.entity.Contact;
import com.daoninhthai.crm.entity.Deal;
import com.daoninhthai.crm.repository.ContactRepository;
import com.daoninhthai.crm.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportService {

    private final ContactRepository contactRepository;
    private final DealRepository dealRepository;

    @Transactional(readOnly = true)
    public String exportContactsToCSV() {
        List<Contact> contacts = contactRepository.findAll();

        StringBuilder csv = new StringBuilder();
        csv.append("ID,First Name,Last Name,Email,Phone,Company,Position,Status,Created At\n");

        for (Contact contact : contacts) {
            csv.append(escapeCSV(contact.getId().toString()));
            csv.append(",");
            csv.append(escapeCSV(contact.getFirstName()));
            csv.append(",");
            csv.append(escapeCSV(contact.getLastName()));
            csv.append(",");
            csv.append(escapeCSV(contact.getEmail()));
            csv.append(",");
            csv.append(escapeCSV(contact.getPhone()));
            csv.append(",");
            csv.append(escapeCSV(contact.getCompany() != null ? contact.getCompany().getName() : ""));
            csv.append(",");
            csv.append(escapeCSV(contact.getPosition()));
            csv.append(",");
            csv.append(escapeCSV(contact.getStatus() != null ? contact.getStatus().name() : ""));
            csv.append(",");
            csv.append(escapeCSV(contact.getCreatedAt() != null ? contact.getCreatedAt().toString() : ""));
            csv.append("\n");
        }

        return csv.toString();
    }

    @Transactional(readOnly = true)
    public String exportDealsToCSV() {
        List<Deal> deals = dealRepository.findAll();

        StringBuilder csv = new StringBuilder();
        csv.append("ID,Title,Value,Stage,Company,Contact,Expected Close Date,Probability,Created At\n");

        for (Deal deal : deals) {
            csv.append(escapeCSV(deal.getId().toString()));
            csv.append(",");
            csv.append(escapeCSV(deal.getTitle()));
            csv.append(",");
            csv.append(escapeCSV(deal.getValue() != null ? deal.getValue().toPlainString() : "0"));
            csv.append(",");
            csv.append(escapeCSV(deal.getStage() != null ? deal.getStage().name() : ""));
            csv.append(",");
            csv.append(escapeCSV(deal.getCompany() != null ? deal.getCompany().getName() : ""));
            csv.append(",");
            csv.append(escapeCSV(deal.getContact() != null ? deal.getContact().getFullName() : ""));
            csv.append(",");
            csv.append(escapeCSV(deal.getExpectedCloseDate() != null ? deal.getExpectedCloseDate().toString() : ""));
            csv.append(",");
            csv.append(escapeCSV(deal.getProbability() != null ? deal.getProbability().toPlainString() : "0"));
            csv.append(",");
            csv.append(escapeCSV(deal.getCreatedAt() != null ? deal.getCreatedAt().toString() : ""));
            csv.append("\n");
        }

        return csv.toString();
    }

    private String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
