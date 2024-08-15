package com.daoninhthai.crm.service;

import com.daoninhthai.crm.dto.CalendarEvent;
import com.daoninhthai.crm.entity.Activity;
import com.daoninhthai.crm.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalendarService {

    private final ActivityRepository activityRepository;
    private final ICalService iCalService;

    @Transactional(readOnly = true)
    public List<CalendarEvent> getActivitiesByDateRange(LocalDateTime from, LocalDateTime to) {
        // Get all activities and filter by date range
        List<Activity> activities = activityRepository.findAll().stream()
                .filter(a -> a.getDueDate() != null)
                .filter(a -> !a.getDueDate().isBefore(from) && !a.getDueDate().isAfter(to))
                .collect(Collectors.toList());

        return activities.stream()
                .map(this::mapToCalendarEvent)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CalendarEvent> getUpcomingActivities(int limit) {
        List<Activity> upcoming = activityRepository.findUpcoming(
                LocalDateTime.now(), Pageable.ofSize(limit));

        return upcoming.stream()
                .map(this::mapToCalendarEvent)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public String generateICalExport(LocalDateTime from, LocalDateTime to) {
        List<CalendarEvent> events = getActivitiesByDateRange(from, to);
        return iCalService.generateICalendar(events);
    }

    @Transactional(readOnly = true)
    public String generateICalForAllActivities() {
        List<Activity> activities = activityRepository.findAll().stream()
                .filter(a -> a.getDueDate() != null)
                .collect(Collectors.toList());

        List<CalendarEvent> events = activities.stream()
                .map(this::mapToCalendarEvent)
                .collect(Collectors.toList());

        return iCalService.generateICalendar(events);
    }

    private CalendarEvent mapToCalendarEvent(Activity activity) {
        String contactName = null;
        String dealName = null;

        if (activity.getContact() != null) {
            contactName = activity.getContact().getFullName();
        }
        if (activity.getDeal() != null) {
            dealName = activity.getDeal().getTitle();
        }

        String typeName = activity.getType() != null ? activity.getType().name() : null;

        return CalendarEvent.builder()
                .id(activity.getId())
                .title(activity.getSubject())
                .start(activity.getDueDate())
                .end(activity.getDueDate() != null ? activity.getDueDate().plusHours(1) : null)
                .type(typeName)
                .contactName(contactName)
                .dealName(dealName)
                .color(CalendarEvent.getColorForType(typeName))
                .completed(activity.isCompleted())
                .description(activity.getDescription())
                .build();
    }
}
