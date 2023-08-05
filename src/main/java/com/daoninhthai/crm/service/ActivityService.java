package com.daoninhthai.crm.service;

import com.daoninhthai.crm.dto.ActivityResponse;
import com.daoninhthai.crm.dto.CreateActivityRequest;
import com.daoninhthai.crm.entity.Activity;
import com.daoninhthai.crm.entity.Contact;
import com.daoninhthai.crm.entity.Deal;
import com.daoninhthai.crm.exception.ResourceNotFoundException;
import com.daoninhthai.crm.mapper.ActivityMapper;
import com.daoninhthai.crm.repository.ActivityRepository;
import com.daoninhthai.crm.repository.ContactRepository;
import com.daoninhthai.crm.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final ContactRepository contactRepository;
    private final DealRepository dealRepository;
    private final ActivityMapper activityMapper;

    @Transactional
    public ActivityResponse create(CreateActivityRequest request) {
        Activity activity = activityMapper.toEntity(request);

        if (request.getContactId() != null) {
            Contact contact = contactRepository.findById(request.getContactId())
                    .orElseThrow(() -> new ResourceNotFoundException("Contact", "id", request.getContactId()));
            activity.setContact(contact);
        }

        if (request.getDealId() != null) {
            Deal deal = dealRepository.findById(request.getDealId())
                    .orElseThrow(() -> new ResourceNotFoundException("Deal", "id", request.getDealId()));
            activity.setDeal(deal);
        }

        Activity saved = activityRepository.save(activity);
        return activityMapper.toResponse(saved);
    }

    @Transactional
    public ActivityResponse update(Long id, CreateActivityRequest request) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity", "id", id));

        activity.setType(request.getType());
        activity.setSubject(request.getSubject());
        activity.setDescription(request.getDescription());
        activity.setDueDate(request.getDueDate());

        if (request.getContactId() != null) {
            Contact contact = contactRepository.findById(request.getContactId())
                    .orElseThrow(() -> new ResourceNotFoundException("Contact", "id", request.getContactId()));
            activity.setContact(contact);
        }

        if (request.getDealId() != null) {
            Deal deal = dealRepository.findById(request.getDealId())
                    .orElseThrow(() -> new ResourceNotFoundException("Deal", "id", request.getDealId()));
            activity.setDeal(deal);
        }

        Activity updated = activityRepository.save(activity);
        return activityMapper.toResponse(updated);
    }

    @Transactional
    public ActivityResponse complete(Long id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity", "id", id));

        activity.setCompleted(true);
        Activity updated = activityRepository.save(activity);
        return activityMapper.toResponse(updated);
    }

    @Transactional(readOnly = true)
    public ActivityResponse getById(Long id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity", "id", id));
        return activityMapper.toResponse(activity);
    }

    @Transactional(readOnly = true)
    public Page<ActivityResponse> getAll(Pageable pageable) {
        return activityRepository.findAll(pageable)
                .map(activityMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<ActivityResponse> getByContact(Long contactId) {
        return activityRepository.findByContactId(contactId).stream()
                .map(activityMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ActivityResponse> getByDeal(Long dealId) {
        return activityRepository.findByDealId(dealId).stream()
                .map(activityMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ActivityResponse> getUpcoming(int limit) {
        Pageable pageable = Pageable.ofSize(limit);
        return activityRepository.findUpcoming(LocalDateTime.now(), pageable).stream()
                .map(activityMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ActivityResponse> getOverdue() {
        return activityRepository.findOverdue(LocalDateTime.now()).stream()
                .map(activityMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        if (!activityRepository.existsById(id)) {
            throw new ResourceNotFoundException("Activity", "id", id);
        }
        activityRepository.deleteById(id);
    }
}
