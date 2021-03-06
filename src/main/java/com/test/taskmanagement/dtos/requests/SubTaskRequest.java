package com.test.taskmanagement.dtos.requests;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubTaskRequest {
    Long id;
    String name;
    String description;
}
