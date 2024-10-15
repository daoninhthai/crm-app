package com.daoninhthai.crm.service;

import com.daoninhthai.crm.dto.BulkOperationResult;
import com.daoninhthai.crm.entity.Contact;
import com.daoninhthai.crm.entity.Deal;
import com.daoninhthai.crm.entity.Tag;
import com.daoninhthai.crm.repository.ContactRepository;
import com.daoninhthai.crm.repository.DealRepository;
import com.daoninhthai.crm.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BulkOperationService {

    private final ContactRepository contactRepository;
    private final DealRepository dealRepository;
    private final TagRepository tagRepository;

    @Transactional
    public BulkOperationResult bulkUpdateContactStatus(List<Long> ids, Contact.ContactStatus status) {
        List<String> errors = new ArrayList<>();
        int successCount = 0;

        for (Long id : ids) {
            try {
                Optional<Contact> contactOpt = contactRepository.findById(id);
                if (contactOpt.isPresent()) {
                    Contact contact = contactOpt.get();
                    contact.setStatus(status);
                    contactRepository.save(contact);
                    successCount++;
                } else {
                    errors.add("Contact not found with id: " + id);
                }
            } catch (Exception e) {
                errors.add("Failed to update contact " + id + ": " + e.getMessage());
            }
        }

        return BulkOperationResult.builder()
                .totalRequested(ids.size())
                .successCount(successCount)
                .failedCount(ids.size() - successCount)
                .errors(errors)
                .build();
    }

    @Transactional
    public BulkOperationResult bulkDeleteContacts(List<Long> ids) {
        List<String> errors = new ArrayList<>();
        int successCount = 0;

        for (Long id : ids) {
            try {
                if (contactRepository.existsById(id)) {
                    contactRepository.deleteById(id);
                    successCount++;
                } else {
                    errors.add("Contact not found with id: " + id);
                }
            } catch (Exception e) {
                errors.add("Failed to delete contact " + id + ": " + e.getMessage());
            }
        }

        return BulkOperationResult.builder()
                .totalRequested(ids.size())
                .successCount(successCount)
                .failedCount(ids.size() - successCount)
                .errors(errors)
                .build();
    }

    @Transactional
    public BulkOperationResult bulkMoveDealStage(List<Long> ids, Deal.DealStage targetStage) {
        List<String> errors = new ArrayList<>();
        int successCount = 0;

        for (Long id : ids) {
            try {
                Optional<Deal> dealOpt = dealRepository.findById(id);
                if (dealOpt.isPresent()) {
                    Deal deal = dealOpt.get();
                    deal.setStage(targetStage);
                    dealRepository.save(deal);
                    successCount++;
                } else {
                    errors.add("Deal not found with id: " + id);
                }
            } catch (Exception e) {
                errors.add("Failed to move deal " + id + ": " + e.getMessage());
            }
        }

        return BulkOperationResult.builder()
                .totalRequested(ids.size())
                .successCount(successCount)
                .failedCount(ids.size() - successCount)
                .errors(errors)
                .build();
    }

    @Transactional
    public BulkOperationResult bulkAssignTag(List<Long> contactIds, Long tagId) {
        List<String> errors = new ArrayList<>();
        int successCount = 0;

        Optional<Tag> tagOpt = tagRepository.findById(tagId);
        if (tagOpt.isEmpty()) {
            errors.add("Tag not found with id: " + tagId);
            return BulkOperationResult.builder()
                    .totalRequested(contactIds.size())
                    .successCount(0)
                    .failedCount(contactIds.size())
                    .errors(errors)
                    .build();
        }

        Tag tag = tagOpt.get();

        for (Long contactId : contactIds) {
            try {
                Optional<Contact> contactOpt = contactRepository.findById(contactId);
                if (contactOpt.isPresent()) {
                    Contact contact = contactOpt.get();
                    contact.getTags().add(tag);
                    contactRepository.save(contact);
                    successCount++;
                } else {
                    errors.add("Contact not found with id: " + contactId);
                }
            } catch (Exception e) {
                errors.add("Failed to assign tag to contact " + contactId + ": " + e.getMessage());
            }
        }

        return BulkOperationResult.builder()
                .totalRequested(contactIds.size())
                .successCount(successCount)
                .failedCount(contactIds.size() - successCount)
                .errors(errors)
                .build();
    }
}
