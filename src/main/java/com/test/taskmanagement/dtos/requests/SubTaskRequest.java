package com.test.taskmanagement.dtos.requests;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubTaskRequest {
    String name;
    String description;
}