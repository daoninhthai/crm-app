package com.daoninhthai.crm.controller;

import com.daoninhthai.crm.dto.DealResponse;
import com.daoninhthai.crm.dto.MoveDealRequest;
import com.daoninhthai.crm.dto.PipelineSummary;
import com.daoninhthai.crm.entity.Deal;
import com.daoninhthai.crm.service.PipelineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pipeline")
@RequiredArgsConstructor
public class PipelineController {

    private final PipelineService pipelineService;

    @GetMapping
    public ResponseEntity<Map<Deal.DealStage, List<DealResponse>>> getPipeline() {
        Map<Deal.DealStage, List<DealResponse>> pipeline = pipelineService.getDealsByStage();
        return ResponseEntity.ok(pipeline);
    }

    @PutMapping("/deals/{id}/move")
    public ResponseEntity<DealResponse> moveDeal(
            @PathVariable Long id,
            @Valid @RequestBody MoveDealRequest request) {
        DealResponse response = pipelineService.moveDeal(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/summary")
    public ResponseEntity<List<PipelineSummary>> getPipelineSummary() {
        List<PipelineSummary> summary = pipelineService.getPipelineSummary();
        return ResponseEntity.ok(summary);
    }
}
