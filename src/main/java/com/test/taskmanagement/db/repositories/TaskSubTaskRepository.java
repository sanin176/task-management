package com.test.taskmanagement.db.repositories;

import com.test.taskmanagement.db.models.TaskSubTask;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface TaskSubTaskRepository extends R2dbcRepository<TaskSubTask, Long> {

}
