package com.test.taskmanagement.db.repositories;

import com.test.taskmanagement.db.models.ExecutorTask;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface ExecutorTaskRepository extends R2dbcRepository<ExecutorTask, Long> {

    Flux<ExecutorTask> findAllByExecutorId(Long taskId);

}
