package com.test.taskmanagement.services;

import com.test.taskmanagement.dtos.SubTaskDto;
import com.test.taskmanagement.dtos.requests.SubTaskRequest;
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
public class SubTaskService {

    public Mono<SubTaskDto> createSubTask(SubTaskRequest subTaskRequest) {
        return Mono.empty();
    }

    public Mono<SubTaskDto> updateSubTask(Long subTaskId, SubTaskRequest subTaskRequest) {
        return Mono.empty();
    }

    public Mono<SubTaskDto> getSubTask(Long subTaskId) {
        return Mono.empty();
    }

    public Mono<Void> deleteSubTask(Long subTaskId) {
        return Mono.empty();
    }
}
