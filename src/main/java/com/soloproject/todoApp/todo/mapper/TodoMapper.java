package com.soloproject.todoApp.todo.mapper;

import com.soloproject.todoApp.todo.dto.TodoDto;
import com.soloproject.todoApp.todo.entity.Todo;
import org.springframework.stereotype.Component;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TodoMapper {
    Todo TodoPostDtoToTodo(TodoDto.Post post);
    Todo TodoPatchDtoToTodo(TodoDto.Patch patch);
    TodoDto.Repsonse TodoToTodoResponseDto(Todo todo);
    List<TodoDto.Repsonse> TodosTodoResponseDtos(List<Todo> todos);
}
