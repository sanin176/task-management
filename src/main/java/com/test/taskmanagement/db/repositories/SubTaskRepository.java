package com.test.taskmanagement.db.repositories;

import com.test.taskmanagement.db.models.SubTask;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface SubTaskRepository extends R2dbcRepository<SubTask, Long> {

}
