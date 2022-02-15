package com.test.taskmanagement.db.repositories;

import com.test.taskmanagement.db.models.Task;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface TaskRepository extends R2dbcRepository<Task, Long> {

}
