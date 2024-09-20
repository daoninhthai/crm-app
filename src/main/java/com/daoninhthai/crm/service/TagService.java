package com.daoninhthai.crm.service;

import com.daoninhthai.crm.dto.TagRequest;
import com.daoninhthai.crm.dto.TagResponse;
import com.daoninhthai.crm.entity.Contact;
import com.daoninhthai.crm.entity.Deal;
import com.daoninhthai.crm.entity.Tag;
import com.daoninhthai.crm.entity.TagType;
import com.daoninhthai.crm.exception.ResourceNotFoundException;
import com.daoninhthai.crm.repository.ContactRepository;
import com.daoninhthai.crm.repository.DealRepository;
import com.daoninhthai.crm.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final ContactRepository contactRepository;
    private final DealRepository dealRepository;

    @Transactional
    public TagResponse createTag(TagRequest request) {
        Tag tag = Tag.builder()
                .name(request.getName())
                .color(request.getColor())
                .type(request.getType())
                .build();
        Tag saved = tagRepository.save(tag);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<TagResponse> getAllTags() {
        return tagRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TagResponse getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", id));
        return toResponse(tag);
    }

    @Transactional(readOnly = true)
    public List<TagResponse> getTagsByType(TagType type) {
        return tagRepository.findByType(type).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tag", "id", id);
        }
        tagRepository.deleteById(id);
    }

    @Transactional
    public void addTagToContact(Long tagId, Long contactId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", tagId));
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new ResourceNotFoundException("Contact", "id", contactId));
        contact.getTags().add(tag);
        contactRepository.save(contact);
    }

    @Transactional
    public void removeTagFromContact(Long tagId, Long contactId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", tagId));
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new ResourceNotFoundException("Contact", "id", contactId));
        contact.getTags().remove(tag);
        contactRepository.save(contact);
    }

    @Transactional
    public void addTagToDeal(Long tagId, Long dealId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", tagId));
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new ResourceNotFoundException("Deal", "id", dealId));
        deal.getTags().add(tag);
        dealRepository.save(deal);
    }

    @Transactional(readOnly = true)
    public List<TagResponse> getTagsForContact(Long contactId) {
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new ResourceNotFoundException("Contact", "id", contactId));
        return contact.getTags().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Set<Long> getContactsByTag(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", tagId));
        return tag.getContacts().stream()
                .map(Contact::getId)
                .collect(Collectors.toSet());
    }

    private TagResponse toResponse(Tag tag) {
        return TagResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .color(tag.getColor())
                .type(tag.getType())
                .build();
    }
}
