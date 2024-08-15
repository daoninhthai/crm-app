package com.daoninhthai.crm.controller;

import com.daoninhthai.crm.dto.CalendarEvent;
import com.daoninhthai.crm.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping
    public ResponseEntity<List<CalendarEvent>> getCalendarEvents(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        List<CalendarEvent> events = calendarService.getActivitiesByDateRange(from, to);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<CalendarEvent>> getUpcoming(
            @RequestParam(defaultValue = "10") int limit) {
        List<CalendarEvent> events = calendarService.getUpcomingActivities(limit);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/export.ics")
    public ResponseEntity<String> exportIcs(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        String icsContent;
        if (from != null && to != null) {
            icsContent = calendarService.generateICalExport(from, to);
        } else {
            icsContent = calendarService.generateICalForAllActivities();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=crm-activities.ics")
                .contentType(MediaType.parseMediaType("text/calendar"))
                .body(icsContent);
    }
}
