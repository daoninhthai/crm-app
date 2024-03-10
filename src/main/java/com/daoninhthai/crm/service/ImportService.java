package com.daoninhthai.crm.service;

import com.daoninhthai.crm.dto.ImportError;
import com.daoninhthai.crm.dto.ImportResult;
import com.daoninhthai.crm.entity.Company;
import com.daoninhthai.crm.entity.Contact;
import com.daoninhthai.crm.repository.CompanyRepository;
import com.daoninhthai.crm.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImportService {

    private final CsvParserService csvParserService;
    private final ContactRepository contactRepository;
    private final CompanyRepository companyRepository;

    private static final int BATCH_SIZE = 50;

    @Transactional
    public ImportResult importContactsFromCsv(InputStream inputStream) throws IOException {
        List<Map<String, String>> records = csvParserService.parse(inputStream);
        ImportResult result = ImportResult.builder()
                .totalRows(records.size())
                .successCount(0)
                .failedCount(0)
                .errors(new ArrayList<>())
                .build();

        List<Contact> batch = new ArrayList<>();
        int rowNumber = 1;

        for (Map<String, String> record : records) {
            rowNumber++;
            try {
                Contact contact = mapToContact(record, rowNumber, result);
                if (contact != null) {
                    batch.add(contact);
                }

                if (batch.size() >= BATCH_SIZE) {
                    contactRepository.saveAll(batch);
                    batch.clear();
                }
            } catch (Exception e) {
                result.addError(ImportError.of(rowNumber, "general", e.getMessage()));
            }
        }

        // Save remaining batch
        if (!batch.isEmpty()) {
            contactRepository.saveAll(batch);
        }

        return result;
    }

    @Transactional
    public ImportResult importCompaniesFromCsv(InputStream inputStream) throws IOException {
        List<Map<String, String>> records = csvParserService.parse(inputStream);
        ImportResult result = ImportResult.builder()
                .totalRows(records.size())
                .successCount(0)
                .failedCount(0)
                .errors(new ArrayList<>())
                .build();

        List<Company> batch = new ArrayList<>();
        int rowNumber = 1;

        for (Map<String, String> record : records) {
            rowNumber++;
            try {
                Company company = mapToCompany(record, rowNumber, result);
                if (company != null) {
                    batch.add(company);
                }

                if (batch.size() >= BATCH_SIZE) {
                    companyRepository.saveAll(batch);
                    batch.clear();
                }
            } catch (Exception e) {
                result.addError(ImportError.of(rowNumber, "general", e.getMessage()));
            }
        }

        // Save remaining batch
        if (!batch.isEmpty()) {
            companyRepository.saveAll(batch);
        }

        return result;
    }

    private Contact mapToContact(Map<String, String> record, int row, ImportResult result) {
        String firstName = record.get("first_name");
        String lastName = record.get("last_name");
        String email = record.get("email");

        // Validate required fields
        if (firstName == null || firstName.isBlank()) {
            result.addError(ImportError.of(row, "first_name", "First name is required"));
            return null;
        }
        if (lastName == null || lastName.isBlank()) {
            result.addError(ImportError.of(row, "last_name", "Last name is required"));
            return null;
        }

        // Check for duplicate email
        if (email != null && !email.isBlank() && contactRepository.existsByEmail(email)) {
            result.addError(ImportError.of(row, "email", "Email already exists: " + email));
            return null;
        }

        Contact contact = Contact.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phone(record.get("phone"))
                .position(record.get("position"))
                .notes(record.get("notes"))
                .build();

        // Parse status if provided
        String status = record.get("status");
        if (status != null && !status.isBlank()) {
            try {
                contact.setStatus(Contact.ContactStatus.valueOf(status.toUpperCase()));
            } catch (IllegalArgumentException e) {
                log.warn("Invalid status '{}' for row {}, using default LEAD", status, row);
            }
        }

        result.incrementSuccess();
        return contact;
    }

    private Company mapToCompany(Map<String, String> record, int row, ImportResult result) {
        String name = record.get("name");

        if (name == null || name.isBlank()) {
            result.addError(ImportError.of(row, "name", "Company name is required"));
            return null;
        }

        Company company = Company.builder()
                .name(name)
                .industry(record.get("industry"))
                .website(record.get("website"))
                .phone(record.get("phone"))
                .address(record.get("address"))
                .build();

        // Parse company size if provided
        String size = record.get("size");
        if (size != null && !size.isBlank()) {
            try {
                company.setSize(Company.CompanySize.valueOf(size.toUpperCase()));
            } catch (IllegalArgumentException e) {
                log.warn("Invalid company size '{}' for row {}, skipping size", size, row);
            }
        }

        result.incrementSuccess();
        return company;
    }
}
