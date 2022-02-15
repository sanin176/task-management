package com.test.taskmanagement.dtos.requests;

import com.test.taskmanagement.db.models.RequestTaskFilter;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchTaskRequest {
    Integer pageNumber;
    Integer pageSize;
    RequestTaskFilter filters;
}
