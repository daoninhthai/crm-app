package com.daoninhthai.crm.controller;

import com.daoninhthai.crm.dto.ActivityResponse;
import com.daoninhthai.crm.dto.CreateActivityRequest;
import com.daoninhthai.crm.service.ActivityService;
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
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping("/api/activities")
    public ResponseEntity<ActivityResponse> create(@Valid @RequestBody CreateActivityRequest request) {
        ActivityResponse response = activityService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/api/activities/{id}")
    public ResponseEntity<ActivityResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateActivityRequest request) {
        ActivityResponse response = activityService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/api/activities/{id}/complete")
    public ResponseEntity<ActivityResponse> complete(@PathVariable Long id) {
        ActivityResponse response = activityService.complete(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/activities/{id}")
    public ResponseEntity<ActivityResponse> getById(@PathVariable Long id) {
        ActivityResponse response = activityService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/activities")
    public ResponseEntity<Page<ActivityResponse>> getAll(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<ActivityResponse> response = activityService.getAll(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/activities/upcoming")
    public ResponseEntity<List<ActivityResponse>> getUpcoming(
            @RequestParam(defaultValue = "10") int limit) {
        List<ActivityResponse> response = activityService.getUpcoming(limit);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/activities/overdue")
    public ResponseEntity<List<ActivityResponse>> getOverdue() {
        List<ActivityResponse> response = activityService.getOverdue();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/contacts/{contactId}/activities")
    public ResponseEntity<List<ActivityResponse>> getByContact(@PathVariable Long contactId) {
        List<ActivityResponse> response = activityService.getByContact(contactId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/deals/{dealId}/activities")
    public ResponseEntity<List<ActivityResponse>> getByDeal(@PathVariable Long dealId) {
        List<ActivityResponse> response = activityService.getByDeal(dealId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/activities/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        activityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
