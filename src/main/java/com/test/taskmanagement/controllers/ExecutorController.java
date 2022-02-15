package com.test.taskmanagement.controllers;

import com.test.taskmanagement.dtos.ExecutorDto;
import com.test.taskmanagement.dtos.requests.ExecutorRequest;
import com.test.taskmanagement.dtos.requests.ExecutorRequestWithoutTasks;
import com.test.taskmanagement.services.ExecutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/task-management/api/v1/executors")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExecutorController {

    ExecutorService executorService;

    @PostMapping
    @Operation(summary = "Create executor")
    @ApiResponse(responseCode = "201")
    public Mono<ExecutorDto> createExecutor(@RequestBody ExecutorRequestWithoutTasks executorRequestWithoutTasks) {
        return executorService.createExecutor(executorRequestWithoutTasks);
    }

    @PutMapping("/{executorId}")
    @Operation(summary = "Update executor by executorId")
    @ApiResponse(responseCode = "200")
    public Mono<ExecutorDto> updateExecutor(@PathVariable Long executorId,
                                            @RequestBody ExecutorRequestWithoutTasks executorRequestWithoutTasks) {
        return executorService.updateExecutor(executorId, executorRequestWithoutTasks);
    }

    @GetMapping("/{executorId}")
    @Operation(summary = "Get executor by executorId")
    @ApiResponse(responseCode = "200")
    public Mono<ExecutorDto> getExecutor(@PathVariable Long executorId) {
        return executorService.getExecutor(executorId);
    }

    @DeleteMapping("/{executorId}")
    @Operation(summary = "Delete executor by executorId")
    @ApiResponse(responseCode = "204")
    public Mono<Void> deleteExecutor(@PathVariable Long executorId) {
        return executorService.deleteExecutor(executorId);
    }

    @PostMapping("/{executorId}")
    @Operation(summary = "Add dependencies tasks for executor")
    @ApiResponse(responseCode = "201")
    public Mono<ExecutorDto> addDependenciesTasksForExecutor(@PathVariable Long executorId,
                                                             @RequestBody ExecutorRequest executorRequest) {
        return executorService.addDependenciesTasksForExecutor(executorId, executorRequest);
    }

}
