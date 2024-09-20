package com.daoninhthai.crm.controller;

import com.daoninhthai.crm.dto.TagRequest;
import com.daoninhthai.crm.dto.TagResponse;
import com.daoninhthai.crm.entity.TagType;
import com.daoninhthai.crm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping
    public ResponseEntity<TagResponse> createTag(@RequestBody TagRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tagService.createTag(request));
    }

    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagResponse> getTagById(@PathVariable Long id) {
        return ResponseEntity.ok(tagService.getTagById(id));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<TagResponse>> getTagsByType(@PathVariable TagType type) {
        return ResponseEntity.ok(tagService.getTagsByType(type));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{tagId}/contacts/{contactId}")
    public ResponseEntity<Void> addTagToContact(@PathVariable Long tagId, @PathVariable Long contactId) {
        tagService.addTagToContact(tagId, contactId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{tagId}/contacts/{contactId}")
    public ResponseEntity<Void> removeTagFromContact(@PathVariable Long tagId, @PathVariable Long contactId) {
        tagService.removeTagFromContact(tagId, contactId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{tagId}/deals/{dealId}")
    public ResponseEntity<Void> addTagToDeal(@PathVariable Long tagId, @PathVariable Long dealId) {
        tagService.addTagToDeal(tagId, dealId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{tagId}/contacts")
    public ResponseEntity<Set<Long>> getContactsByTag(@PathVariable Long tagId) {
        return ResponseEntity.ok(tagService.getContactsByTag(tagId));
    }

    @GetMapping("/contact/{contactId}")
    public ResponseEntity<List<TagResponse>> getTagsForContact(@PathVariable Long contactId) {
        return ResponseEntity.ok(tagService.getTagsForContact(contactId));
    }
}
