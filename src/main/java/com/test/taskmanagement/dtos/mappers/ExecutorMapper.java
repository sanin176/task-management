package com.test.taskmanagement.dtos.mappers;

import com.test.taskmanagement.db.models.Executor;
import com.test.taskmanagement.dtos.ExecutorDto;
import com.test.taskmanagement.dtos.requests.ExecutorRequest;
import com.test.taskmanagement.dtos.requests.ExecutorRequestWithoutTasks;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {TaskMapper.class, SubTaskMapper.class})
public interface ExecutorMapper {

    ExecutorDto toExecutorDto(Executor executor);

    Executor toExecutor(ExecutorRequestWithoutTasks executorRequestWithoutTasks);

    ExecutorDto toExecutorDto(ExecutorRequest executorRequest);

}
