package com.test.taskmanagement.controllers;

import com.test.taskmanagement.dtos.TaskDto;
import com.test.taskmanagement.dtos.requests.SearchTaskRequest;
import com.test.taskmanagement.dtos.requests.TaskRequest;
import com.test.taskmanagement.dtos.requests.TaskRequestWithoutSubTasks;
import com.test.taskmanagement.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
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
@RequestMapping("/task-management/api/v1/tasks")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskController {

    TaskService taskService;

    @PostMapping
    @Operation(summary = "Create task")
    @ApiResponse(responseCode = "201")
    public Mono<TaskDto> createTask(@RequestBody TaskRequestWithoutSubTasks taskRequestWithoutSubTasks) {
        return taskService.createTask(taskRequestWithoutSubTasks);
    }

    @PutMapping("/{taskId}")
    @Operation(summary = "Update task by taskId")
    @ApiResponse(responseCode = "200")
    public Mono<TaskDto> updateTask(@PathVariable Long taskId,
                                    @RequestBody TaskRequestWithoutSubTasks taskRequestWithoutSubTasks) {
        return taskService.updateTask(taskId, taskRequestWithoutSubTasks);
    }

    @GetMapping("/{taskId}")
    @Operation(summary = "Get task by taskId")
    @ApiResponse(responseCode = "200")
    public Mono<TaskDto> getTask(@PathVariable Long taskId) {
        return taskService.getTask(taskId);
    }

    @DeleteMapping("/{taskId}")
    @Operation(summary = "Delete task by taskId")
    @ApiResponse(responseCode = "204")
    public Mono<Void> deleteTask(@PathVariable Long taskId) {
        return taskService.deleteTask(taskId);
    }

    @PostMapping("/{taskId}")
    @Operation(summary = "Add dependencies sub tasks")
    @ApiResponse(responseCode = "201")
    public Mono<TaskDto> addDependenciesSubTasks(@PathVariable Long taskId,
                                                 @RequestBody TaskRequest taskRequest) {
        return taskService.addDependenciesSubTasksForTask(taskId, taskRequest);
    }

    @PostMapping("/employees/search")
    @Operation(summary = "Task search")
    @ApiResponse(responseCode = "200")
    public Mono<Page<TaskDto>> taskSearch(@RequestBody SearchTaskRequest searchTaskRequest) {
        return taskService.taskSearch(searchTaskRequest);
    }

}
