package com.test.taskmanagement.db.repositories;

import com.test.taskmanagement.db.models.Task;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TaskRepository extends R2dbcRepository<Task, Long> {

    Flux<Task> findAllByIdIn(List<Long> ids);

    @Query("SELECT COUNT(t.*) FROM task t " +
            "WHERE (:name IS NULL OR t.name = :name) " +
            "AND (:description IS NULL OR t.description = :description) " +
            "AND (:groupName IS NULL OR t.group_name = :groupName) ")
    Mono<Long> countByFilter(@Param("name") String name,
                             @Param("description") String description,
                             @Param("groupName") String groupName);

    @Query("SELECT t.* FROM task t " +
            "WHERE (:name IS NULL OR t.name = :name) " +
            "AND (:description IS NULL OR t.description = :description) " +
            "AND (:groupName IS NULL OR t.group_name = :groupName) " +
            "ORDER BY t.name " +
            "OFFSET :offset " +
            "LIMIT :limit ")
    Flux<Task> findTaskByFilter(@Param("name") String name,
                                @Param("description") String description,
                                @Param("groupName") String groupName,
                                @Param("offset") long offset,
                                @Param("limit") int limit);

}
