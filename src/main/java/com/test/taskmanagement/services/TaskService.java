package com.test.taskmanagement.services;

import com.test.taskmanagement.dtos.TaskDto;
import com.test.taskmanagement.dtos.requests.TaskRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskService {

    public Mono<TaskDto> createTask(TaskRequest taskRequest) {
        return Mono.empty();
    }

    public Mono<TaskDto> updateTask(Long taskId, TaskRequest taskRequest) {
        return Mono.empty();
    }

    public Mono<TaskDto> getTask(Long taskId) {
        return Mono.empty();
    }

    public Mono<Void> deleteTask(Long taskId) {
        return Mono.empty();
    }
}
