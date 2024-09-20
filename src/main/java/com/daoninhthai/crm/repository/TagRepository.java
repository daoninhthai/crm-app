package com.daoninhthai.crm.repository;

import com.daoninhthai.crm.entity.Tag;
import com.daoninhthai.crm.entity.TagType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findByType(TagType type);

    List<Tag> findByNameContainingIgnoreCase(String name);
}
