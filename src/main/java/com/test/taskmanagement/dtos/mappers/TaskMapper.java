package com.test.taskmanagement.dtos.mappers;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = SubTaskMapper.class)
public interface TaskMapper {

}
