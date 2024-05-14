package org.ahmedukamel.ecommerce.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class BlogPostDtoV3 extends BlogPostDtoV2 {
    private List<String> tags;
}