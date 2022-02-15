package com.test.taskmanagement.db.repositories;

import com.test.taskmanagement.db.models.TaskSubTask;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface TaskSubTaskRepository extends R2dbcRepository<TaskSubTask, Long> {

    Flux<TaskSubTask> findAllByTaskId(Long taskId);

    Flux<TaskSubTask> findAllByTaskIdIn(List<Long> taskId);

}
