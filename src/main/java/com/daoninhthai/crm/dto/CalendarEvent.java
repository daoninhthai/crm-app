package com.daoninhthai.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarEvent {

    private Long id;
    private String title;
    private LocalDateTime start;
    private LocalDateTime end;
    private String type;
    private String contactName;
    private String dealName;
    private String color;
    private boolean completed;
    private String description;

    /**
     * Map activity type to a display color for calendar rendering.
     */
    public static String getColorForType(String type) {
        if (type == null) return "#6B7280";
        return switch (type.toUpperCase()) {
            case "CALL" -> "#3B82F6";      // Blue
            case "EMAIL" -> "#10B981";     // Green
            case "MEETING" -> "#8B5CF6";   // Purple
            case "TASK" -> "#F59E0B";      // Amber
            case "NOTE" -> "#6B7280";      // Gray
            default -> "#6B7280";
        };
    }
}
