package com.test.taskmanagement.controllers;

import com.test.taskmanagement.TaskManagementApplicationTests;
import com.test.taskmanagement.dtos.SubTaskDto;
import com.test.taskmanagement.dtos.requests.SubTaskRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@FieldDefaults(level = AccessLevel.PROTECTED)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SubTaskControllerTest extends TaskManagementApplicationTests {

    static final String SUB_TASK_URI = "/task-management/api/v1/sub-tasks";

    static final String SUCCESS_RESPONSE = "Success response: {}";

    AtomicReference<Long> currentSubTaskId = new AtomicReference(0L);

    @Test
    @Order(1)
    void createSubTask() {
        log.info("createSubTask");

        SubTaskRequest subTaskRequest = SubTaskRequest.builder()
                .name("test")
                .description("test_description")
                .build();

        webTestClient.post()
                .uri(SUB_TASK_URI)
                .bodyValue(subTaskRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SubTaskDto.class)
                .consumeWith(response -> {
                    log.info(SUCCESS_RESPONSE, response);
                    currentSubTaskId.set(Objects.requireNonNull(response.getResponseBody()).getId());
                    Assertions.assertEquals("test", response.getResponseBody().getName());
                });

    }

    @Test
    @Order(2)
    void updateSubTask() {
        log.info("updateSubTask");

        SubTaskRequest newSubTaskRequest = SubTaskRequest.builder()
                .name("test-1")
                .description("test_description")
                .build();

        webTestClient.put()
                .uri(SUB_TASK_URI + "/" + currentSubTaskId.get())
                .bodyValue(newSubTaskRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SubTaskDto.class)
                .consumeWith(response -> {
                    log.info(SUCCESS_RESPONSE, response);
                    Assertions.assertEquals("test-1", Objects.requireNonNull(response.getResponseBody()).getName());
                });
    }

    @Test
    @Order(3)
    void getSubTask() {
        log.info("getSubTask");

        webTestClient.get()
                .uri(SUB_TASK_URI + "/" + currentSubTaskId.get())
                .exchange()
                .expectStatus().isOk()
                .expectBody(SubTaskDto.class)
                .consumeWith(response -> {
                    log.info(SUCCESS_RESPONSE, response);
                    Assertions.assertEquals("test-1", Objects.requireNonNull(response.getResponseBody()).getName());
                });
    }

    @Test
    @Order(4)
    void deleteSubTask() {
        log.info("deleteSubTask");

        webTestClient.delete()
                .uri(SUB_TASK_URI + "/" + currentSubTaskId.get())
                .exchange()
                .expectStatus().isOk();
    }

}
