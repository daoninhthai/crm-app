package com.daoninhthai.crm.controller;

import com.daoninhthai.crm.dto.BulkDeleteRequest;
import com.daoninhthai.crm.dto.BulkMoveDealRequest;
import com.daoninhthai.crm.dto.BulkOperationResult;
import com.daoninhthai.crm.dto.BulkUpdateStatusRequest;
import com.daoninhthai.crm.service.BulkOperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bulk")
@RequiredArgsConstructor
public class BulkController {

    private final BulkOperationService bulkOperationService;

    @PostMapping("/contacts/update-status")
    public ResponseEntity<BulkOperationResult> bulkUpdateContactStatus(
            @RequestBody BulkUpdateStatusRequest request) {
        BulkOperationResult result = bulkOperationService.bulkUpdateContactStatus(
                request.getIds(), request.getStatus());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/contacts/delete")
    public ResponseEntity<BulkOperationResult> bulkDeleteContacts(
            @RequestBody BulkDeleteRequest request) {
        BulkOperationResult result = bulkOperationService.bulkDeleteContacts(request.getIds());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/deals/move-stage")
    public ResponseEntity<BulkOperationResult> bulkMoveDealStage(
            @RequestBody BulkMoveDealRequest request) {
        BulkOperationResult result = bulkOperationService.bulkMoveDealStage(
                request.getIds(), request.getTargetStage());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/deals/assign-tag")
    public ResponseEntity<BulkOperationResult> bulkAssignTag(
            @RequestParam Long tagId,
            @RequestBody BulkDeleteRequest request) {
        BulkOperationResult result = bulkOperationService.bulkAssignTag(request.getIds(), tagId);
        return ResponseEntity.ok(result);
    }
}
