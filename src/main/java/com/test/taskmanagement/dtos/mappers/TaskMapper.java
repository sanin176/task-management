package com.test.taskmanagement.dtos.mappers;

import com.test.taskmanagement.db.models.Task;
import com.test.taskmanagement.dtos.TaskDto;
import com.test.taskmanagement.dtos.requests.TaskRequest;
import com.test.taskmanagement.dtos.requests.TaskRequestWithoutSubTasks;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = SubTaskMapper.class)
public interface TaskMapper {

    TaskDto toTaskDto(Task task);

    @Mapping(target = "id", ignore = true)
    Task toTask(TaskRequestWithoutSubTasks taskRequestWithoutSubTasks);

    TaskDto toTaskDto(TaskRequest taskRequest);
}
