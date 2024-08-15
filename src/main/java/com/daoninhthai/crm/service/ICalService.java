package com.daoninhthai.crm.service;

import com.daoninhthai.crm.dto.CalendarEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * Service for generating iCalendar (.ics) format files.
 * Follows RFC 5545 specification.
 */
@Service
@Slf4j
public class ICalService {

    private static final DateTimeFormatter ICAL_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
    private static final String CRLF = "\r\n";

    /**
     * Generate a complete iCalendar file from a list of calendar events.
     */
    public String generateICalendar(List<CalendarEvent> events) {
        StringBuilder ical = new StringBuilder();

        // Calendar header
        ical.append("BEGIN:VCALENDAR").append(CRLF);
        ical.append("VERSION:2.0").append(CRLF);
        ical.append("PRODID:-//CRM Application//EN").append(CRLF);
        ical.append("CALSCALE:GREGORIAN").append(CRLF);
        ical.append("METHOD:PUBLISH").append(CRLF);
        ical.append("X-WR-CALNAME:CRM Activities").append(CRLF);

        // Add each event
        for (CalendarEvent event : events) {
            ical.append(generateEvent(event));
        }

        // Calendar footer
        ical.append("END:VCALENDAR").append(CRLF);

        return ical.toString();
    }

    /**
     * Generate a single iCalendar event (VEVENT).
     */
    public String generateEvent(CalendarEvent event) {
        StringBuilder vevent = new StringBuilder();

        vevent.append("BEGIN:VEVENT").append(CRLF);
        vevent.append("UID:").append(UUID.randomUUID().toString()).append("@crm-app").append(CRLF);
        vevent.append("DTSTAMP:").append(formatDateTime(LocalDateTime.now())).append(CRLF);

        if (event.getStart() != null) {
            vevent.append("DTSTART:").append(formatDateTime(event.getStart())).append(CRLF);
        }

        if (event.getEnd() != null) {
            vevent.append("DTEND:").append(formatDateTime(event.getEnd())).append(CRLF);
        } else if (event.getStart() != null) {
            // Default 1-hour duration
            vevent.append("DTEND:").append(formatDateTime(event.getStart().plusHours(1))).append(CRLF);
        }

        vevent.append("SUMMARY:").append(escapeICalText(event.getTitle())).append(CRLF);

        if (event.getDescription() != null) {
            vevent.append("DESCRIPTION:").append(escapeICalText(event.getDescription())).append(CRLF);
        }

        // Add categories based on type
        if (event.getType() != null) {
            vevent.append("CATEGORIES:").append(event.getType()).append(CRLF);
        }

        // Add contact and deal info in description
        StringBuilder notes = new StringBuilder();
        if (event.getContactName() != null) {
            notes.append("Contact: ").append(event.getContactName());
        }
        if (event.getDealName() != null) {
            if (notes.length() > 0) notes.append("\\n");
            notes.append("Deal: ").append(event.getDealName());
        }
        if (notes.length() > 0) {
            vevent.append("COMMENT:").append(notes).append(CRLF);
        }

        if (event.isCompleted()) {
            vevent.append("STATUS:COMPLETED").append(CRLF);
        } else {
            vevent.append("STATUS:CONFIRMED").append(CRLF);
        }

        vevent.append("END:VEVENT").append(CRLF);

        return vevent.toString();
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(ICAL_FORMAT);
    }

    private String escapeICalText(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                   .replace(",", "\\,")
                   .replace(";", "\\;")
                   .replace("\n", "\\n");
    }
}
