package com.test.taskmanagement.controllers;

import com.test.taskmanagement.dtos.SubTaskDto;
import com.test.taskmanagement.dtos.requests.SubTaskRequest;
import com.test.taskmanagement.services.SubTaskService;
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
@RequestMapping("/task-management/api/v1/sub-tasks")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubTaskController {

    SubTaskService subTaskService;

    @PostMapping("/")
    @Operation(summary = "Create sub task")
    @ApiResponse(responseCode = "201")
    public Mono<SubTaskDto> createSubTask(@RequestBody SubTaskRequest subTaskRequest) {
        return subTaskService.createSubTask(subTaskRequest);
    }

    @PutMapping("/{subTaskId}")
    @Operation(summary = "Update sub task by subTaskId")
    @ApiResponse(responseCode = "200")
    public Mono<SubTaskDto> updateSubTask(@PathVariable Long subTaskId,
                                          @RequestBody SubTaskRequest subTaskRequest) {
        return subTaskService.updateSubTask(subTaskId, subTaskRequest);
    }

    @GetMapping("/{subTaskId}")
    @Operation(summary = "Get sub task by subTaskId")
    @ApiResponse(responseCode = "200")
    public Mono<SubTaskDto> getSubTask(@PathVariable Long subTaskId) {
        return subTaskService.getSubTask(subTaskId);
    }

    @DeleteMapping("/{subTaskId}")
    @Operation(summary = "Delete sub task by subTaskId")
    @ApiResponse(responseCode = "204")
    public Mono<Void> deleteSubTask(@PathVariable Long subTaskId) {
        return subTaskService.deleteSubTask(subTaskId);
    }

}
