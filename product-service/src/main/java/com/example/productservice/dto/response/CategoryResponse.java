package com.example.productservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse {
    String id;
    String name;
    CategoryResponse parent;
    Boolean isDeleted;
    Instant createdAt;
    String createdBy;
    Instant updatedAt;
    String updatedBy;
}
