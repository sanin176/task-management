package com.test.taskmanagement.dtos.requests;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskRequestWithoutSubTasks {
    Long id;
    String name;
    String description;
    String groupName;
    Boolean assignee;
}
