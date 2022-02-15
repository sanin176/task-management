package com.test.taskmanagement.services;

import com.test.taskmanagement.dtos.ExecutorDto;
import com.test.taskmanagement.dtos.requests.ExecutorRequest;
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
public class ExecutorService {

    public Mono<ExecutorDto> createExecutor(ExecutorRequest executorRequest) {
        return Mono.empty();
    }

    public Mono<ExecutorDto> updateExecutor(Long executorId, ExecutorRequest executorRequest) {
        return Mono.empty();
    }

    public Mono<ExecutorDto> getExecutor(Long executorId) {
        return Mono.empty();
    }

    public Mono<Void> deleteExecutor(Long executorId) {
        return Mono.empty();
    }
}
