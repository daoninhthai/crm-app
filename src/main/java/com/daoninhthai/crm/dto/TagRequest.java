package com.daoninhthai.crm.dto;

import com.daoninhthai.crm.entity.TagType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagRequest {
    private String name;
    private String color;
    private TagType type;
}
