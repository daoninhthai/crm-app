package com.daoninhthai.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportResult {

    private int totalRows;
    private int successCount;
    private int failedCount;

    @Builder.Default
    private List<ImportError> errors = new ArrayList<>();

    public void addError(ImportError error) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(error);
        failedCount++;
    }

    public void incrementSuccess() {
        successCount++;
    }
}
