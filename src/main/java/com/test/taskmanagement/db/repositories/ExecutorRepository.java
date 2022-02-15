package com.test.taskmanagement.db.repositories;

import com.test.taskmanagement.db.models.Executor;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ExecutorRepository extends R2dbcRepository<Executor, Long> {

}
