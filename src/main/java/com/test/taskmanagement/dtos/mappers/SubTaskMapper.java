package com.test.taskmanagement.dtos.mappers;

import com.test.taskmanagement.db.models.SubTask;
import com.test.taskmanagement.dtos.SubTaskDto;
import com.test.taskmanagement.dtos.requests.SubTaskRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubTaskMapper {

    SubTaskDto toSubTaskDto(SubTask subTask);

    @Mapping(target = "id", ignore = true)
    SubTask toSubTask(SubTaskRequest subTaskRequest);

}
