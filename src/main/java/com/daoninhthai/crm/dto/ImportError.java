package com.daoninhthai.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportError {

    private int row;
    private String field;
    private String message;

    public static ImportError of(int row, String field, String message) {
        return ImportError.builder()
                .row(row)
                .field(field)
                .message(message)
                .build();
    }
}
