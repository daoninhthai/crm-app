package com.daoninhthai.crm.controller;

import com.daoninhthai.crm.dto.CreateDealRequest;
import com.daoninhthai.crm.dto.DealResponse;
import com.daoninhthai.crm.dto.UpdateDealRequest;
import com.daoninhthai.crm.entity.Deal;
import com.daoninhthai.crm.service.DealService;
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
@RequestMapping("/api/deals")
@RequiredArgsConstructor
public class DealController {

    private final DealService dealService;

    @PostMapping
    public ResponseEntity<DealResponse> create(@Valid @RequestBody CreateDealRequest request) {
        DealResponse response = dealService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DealResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDealRequest request) {
        DealResponse response = dealService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DealResponse> getById(@PathVariable Long id) {
        DealResponse response = dealService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<DealResponse>> getAll(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<DealResponse> response = dealService.getAll(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stage/{stage}")
    public ResponseEntity<List<DealResponse>> getByStage(@PathVariable Deal.DealStage stage) {
        List<DealResponse> response = dealService.getByStage(stage);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<DealResponse>> getByCompany(@PathVariable Long companyId) {
        List<DealResponse> response = dealService.getByCompany(companyId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/contact/{contactId}")
    public ResponseEntity<List<DealResponse>> getByContact(@PathVariable Long contactId) {
        List<DealResponse> response = dealService.getByContact(contactId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        dealService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
