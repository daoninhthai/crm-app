package com.daoninhthai.crm.controller;

import com.daoninhthai.crm.dto.ContactResponse;
import com.daoninhthai.crm.dto.CreateContactRequest;
import com.daoninhthai.crm.dto.UpdateContactRequest;
import com.daoninhthai.crm.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<ContactResponse> create(@Valid @RequestBody CreateContactRequest request) {
        ContactResponse response = contactService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateContactRequest request) {
        ContactResponse response = contactService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactResponse> getById(@PathVariable Long id) {
        ContactResponse response = contactService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<ContactResponse>> getAll(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<ContactResponse> response = contactService.getAll(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ContactResponse>> search(
            @RequestParam String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<ContactResponse> response = contactService.search(keyword, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<ContactResponse>> getByCompany(@PathVariable Long companyId) {
        List<ContactResponse> response = contactService.getByCompany(companyId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        contactService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
