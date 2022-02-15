package com.test.taskmanagement.services;

import com.test.taskmanagement.db.models.SubTask;
import com.test.taskmanagement.db.models.Task;
import com.test.taskmanagement.db.models.TaskSubTask;
import com.test.taskmanagement.db.repositories.SubTaskRepository;
import com.test.taskmanagement.db.repositories.TaskRepository;
import com.test.taskmanagement.db.repositories.TaskSubTaskRepository;
import com.test.taskmanagement.dtos.TaskDto;
import com.test.taskmanagement.dtos.mappers.TaskMapper;
import com.test.taskmanagement.dtos.requests.SearchTaskRequest;
import com.test.taskmanagement.dtos.requests.SubTaskRequest;
import com.test.taskmanagement.dtos.requests.TaskRequest;
import com.test.taskmanagement.dtos.requests.TaskRequestWithoutSubTasks;
import com.test.taskmanagement.exceptions.NotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskService {

    String taskDoesNotExist = "Task does not exist.";

    TaskRepository taskRepository;
    TaskSubTaskRepository taskSubTaskRepository;
    SubTaskRepository subTaskRepository;

    TaskMapper taskMapper;

    public Mono<TaskDto> createTask(TaskRequestWithoutSubTasks taskRequestWithoutSubTasks) {
        return taskRepository.save(taskMapper.toTask(taskRequestWithoutSubTasks))
                .map(taskMapper::toTaskDto)
                .doOnSuccess(res -> log.info("Task was created with id = {}.", res.getId()))
                .doOnError(error -> log.error("Task not created."));
    }

    public Mono<TaskDto> updateTask(Long taskId, TaskRequestWithoutSubTasks taskRequestWithoutSubTasks) {
        return taskRepository.findById(taskId)
                .switchIfEmpty(Mono.error(new NotFoundException(taskDoesNotExist)))
                .flatMap(task -> taskRepository.save(task.setName(taskRequestWithoutSubTasks.getName())
                        .setDescription(taskRequestWithoutSubTasks.getDescription())
                        .setGroupName(taskRequestWithoutSubTasks.getGroupName())))
                .map(taskMapper::toTaskDto)
                .doOnSuccess(res -> log.info("Task was updated with id = {}.", taskId))
                .doOnError(error -> log.error("Task not updated with id = {}.", taskId));
    }

    public Mono<TaskDto> getTask(Long taskId) {
        return taskRepository.findById(taskId)
                .switchIfEmpty(Mono.error(new NotFoundException(taskDoesNotExist)))
                .flatMap(task -> taskSubTaskRepository.findAllByTaskId(taskId)
                        .collectList()
                        .flatMap(taskSubTasks -> subTaskRepository.findAllByIdIn(taskSubTasks.stream()
                                .map(TaskSubTask::getSubTaskId)
                                .collect(Collectors.toList()))
                                .collectList()
                                .map(task::setSubTaskList)))
                .map(taskMapper::toTaskDto)
                .doOnSuccess(res -> log.info("Task was got with id = {}.", taskId))
                .doOnError(error -> log.error("Task not got with id = {}.", taskId));
    }

    public Mono<Void> deleteTask(Long taskId) {
        return taskRepository.findById(taskId)
                .switchIfEmpty(Mono.error(new NotFoundException(taskDoesNotExist)))
                .flatMap(task -> taskRepository.deleteById(taskId))
                .doOnSuccess(res -> log.info("Task was deleted with id = {}.", taskId))
                .doOnError(error -> log.error("Task not deleted with id = {}.", taskId));
    }

    public Mono<TaskDto> addDependenciesSubTasksForTask(Long taskId, TaskRequest taskRequest) {
        return Mono.zip(taskRepository.findById(taskId)
                        .switchIfEmpty(Mono.error(new NotFoundException(taskDoesNotExist))),
                taskSubTaskRepository.findAllByTaskId(taskId)
                        .collectList(),
                subTaskRepository.findAllByIdIn(taskRequest.getSubTaskList().stream()
                        .map(SubTaskRequest::getId)
                        .collect(Collectors.toList()))
                        .collectList())
                .flatMap(TupleUtils.function((task, oldTaskSubTaskList, newSubTaskList) -> {

                    Mono<Void> result = Mono.empty();

                    List<TaskSubTask> listForDelete = oldTaskSubTaskList.stream()
                            .filter(oldTaskSubTask -> newSubTaskList.stream()
                                    .noneMatch(newSubTask -> newSubTask.getId().equals(oldTaskSubTask.getSubTaskId())))
                            .collect(Collectors.toList());

                    if (!listForDelete.isEmpty()) {
                        result = result.then(taskSubTaskRepository.deleteAll(listForDelete));
                    }

                    List<TaskSubTask> listForAdd = newSubTaskList.stream()
                            .filter(newSubTask -> oldTaskSubTaskList.stream()
                                    .noneMatch(oldTaskSubTask -> oldTaskSubTask.getId().equals(newSubTask.getId())))
                            .map(newSubTask -> TaskSubTask.builder()
                                    .taskId(taskId)
                                    .subTaskId(newSubTask.getId())
                                    .build())
                            .collect(Collectors.toList());

                    if (!listForAdd.isEmpty()) {
                        result = result.then(taskSubTaskRepository.saveAll(listForAdd).then());
                    }

                    return result.thenReturn(taskMapper.toTaskDto(taskRequest));
                }))
                .doOnSuccess(res -> log.info("Added dependencies for task with id = {}.", taskId))
                .doOnError(error -> log.error("Not added dependencies for task with id = {}.", taskId));
    }

    public Mono<Page<TaskDto>> taskSearch(SearchTaskRequest searchTaskRequest) {
        final Pageable pageable = PageRequest.of(searchTaskRequest.getPageNumber(),
                searchTaskRequest.getPageSize());
        AtomicReference<String> name = new AtomicReference<>(null);
        AtomicReference<String> description = new AtomicReference<>(null);
        AtomicReference<String> groupName = new AtomicReference<>(null);
        if (searchTaskRequest.getFilters() != null) {
            name.set(searchTaskRequest.getFilters().getName());
            description.set(searchTaskRequest.getFilters().getDescription());
            groupName.set(searchTaskRequest.getFilters().getGroupName());
        }

        return taskRepository.countByFilter(name.get(), description.get(), groupName.get())
                .flatMap(countTask -> taskRepository.findTaskByFilter(name.get(),
                        description.get(), groupName.get(),
                        pageable.getOffset(), pageable.getPageSize())
                        .collectList()
                        .flatMap(tasks -> taskSubTaskRepository.findAllByTaskIdIn(tasks.stream()
                                .map(Task::getId)
                                .collect(Collectors.toList()))
                                .collectList()
                                .flatMap(taskSubTaskList -> subTaskRepository.findAllByIdIn(taskSubTaskList.stream()
                                        .map(TaskSubTask::getSubTaskId)
                                        .collect(Collectors.toList()))
                                        .collectList()
                                        .map(subTaskList -> {
                                            List<TaskDto> resultTaskDtos = tasks.stream().map(task -> {
                                                List<SubTask> resSubTaskList = taskSubTaskList.stream()
                                                        .filter(taskSubTask -> taskSubTask.getTaskId().equals(task.getId()))
                                                        .map(taskSubTask -> subTaskList.stream()
                                                                .filter(subTask -> subTask.getId().equals(taskSubTask.getSubTaskId()))
                                                                .findFirst().get())
                                                        .collect(Collectors.toList());
                                                return taskMapper.toTaskDto(
                                                        task.setSubTaskList(resSubTaskList));
                                            }).collect(Collectors.toList());
                                            return new PageImpl<>(resultTaskDtos, pageable, countTask);
                                        }))));
    }
}
