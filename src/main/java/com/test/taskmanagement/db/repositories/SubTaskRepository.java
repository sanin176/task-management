package com.test.taskmanagement.db.repositories;

import com.test.taskmanagement.db.models.SubTask;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface SubTaskRepository extends R2dbcRepository<SubTask, Long> {

    Flux<SubTask> findAllByIdIn(List<Long> ids);

}
