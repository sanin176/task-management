package com.test.taskmanagement.services;

import com.test.taskmanagement.db.models.Executor;
import com.test.taskmanagement.db.models.ExecutorTask;
import com.test.taskmanagement.db.models.SubTask;
import com.test.taskmanagement.db.models.Task;
import com.test.taskmanagement.db.models.TaskSubTask;
import com.test.taskmanagement.db.repositories.ExecutorRepository;
import com.test.taskmanagement.db.repositories.ExecutorTaskRepository;
import com.test.taskmanagement.db.repositories.SubTaskRepository;
import com.test.taskmanagement.db.repositories.TaskRepository;
import com.test.taskmanagement.db.repositories.TaskSubTaskRepository;
import com.test.taskmanagement.dtos.ExecutorDto;
import com.test.taskmanagement.dtos.mappers.ExecutorMapper;
import com.test.taskmanagement.dtos.requests.ExecutorRequest;
import com.test.taskmanagement.dtos.requests.ExecutorRequestWithoutTasks;
import com.test.taskmanagement.dtos.requests.TaskRequest;
import com.test.taskmanagement.exceptions.NotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExecutorService {

    String executorDoesNotExist = "Executor does not exist.";

    ExecutorRepository executorRepository;
    ExecutorTaskRepository executorTaskRepository;
    TaskRepository taskRepository;
    TaskSubTaskRepository taskSubTaskRepository;
    SubTaskRepository subTaskRepository;

    ExecutorMapper executorMapper;

    public Mono<ExecutorDto> createExecutor(ExecutorRequestWithoutTasks executorRequestWithoutTasks) {
        return executorRepository.save(executorMapper.toExecutor(executorRequestWithoutTasks))
                .map(executorMapper::toExecutorDto)
                .doOnSuccess(res -> log.info("Executor was created with id = {}.", res.getId()))
                .doOnError(error -> log.error("Executor not created."));
    }

    public Mono<ExecutorDto> updateExecutor(Long executorId, ExecutorRequestWithoutTasks executorRequestWithoutTasks) {
        return executorRepository.findById(executorId)
                .switchIfEmpty(Mono.error(new NotFoundException(executorDoesNotExist)))
                .flatMap(executor -> executorRepository.save(executor.setFirstName(executorRequestWithoutTasks.getFirstName())
                        .setLastName(executorRequestWithoutTasks.getLastName())))
                .map(executorMapper::toExecutorDto)
                .doOnSuccess(res -> log.info("Executor was updated with id = {}.", executorId))
                .doOnError(error -> log.error("Executor not updated with id = {}.", executorId));
    }

    public Mono<ExecutorDto> getExecutor(Long executorId) {
        return executorRepository.findById(executorId)
                .switchIfEmpty(Mono.error(new NotFoundException(executorDoesNotExist)))
                .flatMap(executor -> executorTaskRepository.findAllByExecutorId(executorId)
                        .collectList()
                        .flatMap(executorTasks -> taskRepository.findAllById(executorTasks.stream()
                                .map(ExecutorTask::getTaskId)
                                .collect(Collectors.toList()))
                                .collectList()
                                .flatMap(tasks -> setTasksWithSubTasks(executor, tasks))))
                .map(executorMapper::toExecutorDto)
                .doOnSuccess(res -> log.info("Executor was got with id = {}.", executorId))
                .doOnError(error -> log.error("Executor not got with id = {}.", executorId));
    }

    private Mono<Executor> setTasksWithSubTasks(Executor executor,
                                                List<Task> taskList) {
        return taskSubTaskRepository.findAllByTaskIdIn(taskList.stream()
                .map(Task::getId)
                .collect(Collectors.toList()))
                .collectList()
                .flatMap(taskSubTasks -> subTaskRepository.findAllByIdIn(taskSubTasks.stream()
                        .map(TaskSubTask::getSubTaskId)
                        .collect(Collectors.toList()))
                        .collectList()
                        .map(subTasks -> {
                            taskList.forEach(task -> {
                                List<SubTask> subTaskList = taskSubTasks.stream()
                                        .filter(taskSubTask -> taskSubTask.getTaskId().equals(task.getId()))
                                        .map(taskSubTask -> subTasks.stream()
                                                .filter(subTask -> subTask.getId().equals(taskSubTask.getSubTaskId()))
                                                .findFirst().get())
                                        .collect(Collectors.toList());
                                task.setSubTaskList(subTaskList);
                            });
                            return executor.setTaskList(taskList);
                        }));
    }

    public Mono<Void> deleteExecutor(Long executorId) {
        return executorRepository.findById(executorId)
                .switchIfEmpty(Mono.error(new NotFoundException(executorDoesNotExist)))
                .flatMap(executor -> executorRepository.deleteById(executorId))
                .doOnSuccess(res -> log.info("Executor was deleted with id = {}.", executorId))
                .doOnError(error -> log.error("Executor not deleted with id = {}.", executorId));
    }

    public Mono<ExecutorDto> addDependenciesTasksForExecutor(Long executorId, ExecutorRequest executorRequest) {
        return Mono.zip(executorRepository.findById(executorId)
                        .switchIfEmpty(Mono.error(new NotFoundException(executorDoesNotExist))),
                executorTaskRepository.findAllByExecutorId(executorId)
                        .collectList(),
                taskRepository.findAllByIdIn(executorRequest.getTaskRequestList().stream()
                        .map(TaskRequest::getId)
                        .collect(Collectors.toList()))
                        .collectList())
                .flatMap(TupleUtils.function((executor, oldExecutorTaskList, newTaskList) -> {

                    Mono<Void> result = Mono.empty();

                    List<ExecutorTask> listForDelete = oldExecutorTaskList.stream()
                            .filter(oldExecutorTask -> newTaskList.stream()
                                    .noneMatch(newTask -> newTask.getId().equals(oldExecutorTask.getTaskId())))
                            .collect(Collectors.toList());

                    if (!listForDelete.isEmpty()) {
                        result = result.then(executorTaskRepository.deleteAll(listForDelete));
                    }

                    List<ExecutorTask> listForAdd = newTaskList.stream()
                            .filter(newTask -> oldExecutorTaskList.stream()
                                    .noneMatch(oldExecutorTask -> oldExecutorTask.getTaskId().equals(newTask.getId())))
                            .map(newTask -> ExecutorTask.builder()
                                    .executorId(executorId)
                                    .taskId(newTask.getId())
                                    .build())
                            .collect(Collectors.toList());

                    if (!listForAdd.isEmpty()) {
                        result = result.then(executorTaskRepository.saveAll(listForAdd).then());
                    }

                    return result.thenReturn(executorMapper.toExecutorDto(executorRequest));
                }))
                .doOnSuccess(res -> log.info("Added dependencies for executor with id = {}.", executorId))
                .doOnError(error -> log.error("Not added dependencies for executor with id = {}.", executorId));
    }

}
