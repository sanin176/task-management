package com.test.taskmanagement.controllers;

import com.test.taskmanagement.TaskManagementApplicationTests;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

@Slf4j
@FieldDefaults(level = AccessLevel.PROTECTED)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskControllerTest extends TaskManagementApplicationTests {

}
