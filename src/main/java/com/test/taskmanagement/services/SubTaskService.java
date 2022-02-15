package com.test.taskmanagement.services;

import com.test.taskmanagement.db.repositories.SubTaskRepository;
import com.test.taskmanagement.dtos.SubTaskDto;
import com.test.taskmanagement.dtos.mappers.SubTaskMapper;
import com.test.taskmanagement.dtos.requests.SubTaskRequest;
import com.test.taskmanagement.exceptions.NotFoundException;
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

    String subTaskDoesNotExist = "SubTask does not exist.";

    SubTaskRepository subTaskRepository;

    SubTaskMapper subTaskMapper;

    public Mono<SubTaskDto> createSubTask(SubTaskRequest subTaskRequest) {
        return subTaskRepository.save(subTaskMapper.toSubTask(subTaskRequest))
                .map(subTaskMapper::toSubTaskDto)
                .doOnSuccess(res -> log.info("SubTask was created with id = {}.", res.getId()))
                .doOnError(error -> log.error("SubTask not created."));
    }

    public Mono<SubTaskDto> updateSubTask(Long subTaskId, SubTaskRequest subTaskRequest) {
        return subTaskRepository.findById(subTaskId)
                .switchIfEmpty(Mono.error(new NotFoundException(subTaskDoesNotExist)))
                .flatMap(subTask -> subTaskRepository.save(subTask.setName(subTaskRequest.getName())
                        .setDescription(subTaskRequest.getDescription())))
                .map(subTaskMapper::toSubTaskDto)
                .doOnSuccess(res -> log.info("SubTask was updated with id = {}.", subTaskId))
                .doOnError(error -> log.error("SubTask not updated with id = {}.", subTaskId));
    }

    public Mono<SubTaskDto> getSubTask(Long subTaskId) {
        return subTaskRepository.findById(subTaskId)
                .switchIfEmpty(Mono.error(new NotFoundException(subTaskDoesNotExist)))
                .map(subTaskMapper::toSubTaskDto)
                .doOnSuccess(res -> log.info("SubTask was got with id = {}.", subTaskId))
                .doOnError(error -> log.error("SubTask not got with id = {}.", subTaskId));
    }

    public Mono<Void> deleteSubTask(Long subTaskId) {
        return subTaskRepository.findById(subTaskId)
                .switchIfEmpty(Mono.error(new NotFoundException(subTaskDoesNotExist)))
                .flatMap(subTask -> subTaskRepository.deleteById(subTaskId))
                .doOnSuccess(res -> log.info("SubTask was deleted with id = {}.", subTaskId))
                .doOnError(error -> log.error("SubTask not deleted with id = {}.", subTaskId));
    }
}
