package com.test.taskmanagement.dtos;

import com.test.taskmanagement.db.models.Task;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExecutorDto {
    Long id;
    String firstName;
    String lastName;
    List<TaskDto> taskList;
}
