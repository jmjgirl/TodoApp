package com.soloproject.todoApp.todo.mapper;

import com.soloproject.todoApp.todo.dto.TodoDto;
import com.soloproject.todoApp.todo.entity.Todo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-05T16:21:08+0900",
    comments = "version: 1.5.1.Final, compiler: javac, environment: Java 11.0.17 (Oracle Corporation)"
)
@Component
public class TodoMapperImpl implements TodoMapper {

    @Override
    public Todo TodoPostDtoToTodo(TodoDto.Post post) {
        if ( post == null ) {
            return null;
        }

        Todo todo = new Todo();

        todo.setTitle( post.getTitle() );
        todo.setTodoOrder( post.getTodoOrder() );
        todo.setCompleted( post.isCompleted() );

        return todo;
    }

    @Override
    public Todo TodoPatchDtoToTodo(TodoDto.Patch patch) {
        if ( patch == null ) {
            return null;
        }

        Todo todo = new Todo();

        todo.setId( patch.getId() );
        todo.setTitle( patch.getTitle() );
        todo.setTodoOrder( patch.getTodoOrder() );
        todo.setCompleted( patch.isCompleted() );

        return todo;
    }

    @Override
    public TodoDto.Repsonse TodoToTodoResponseDto(Todo todo) {
        if ( todo == null ) {
            return null;
        }

        TodoDto.Repsonse.RepsonseBuilder repsonse = TodoDto.Repsonse.builder();

        if ( todo.getId() != null ) {
            repsonse.id( todo.getId() );
        }
        repsonse.title( todo.getTitle() );
        repsonse.todoOrder( todo.getTodoOrder() );
        repsonse.completed( todo.isCompleted() );

        return repsonse.build();
    }

    @Override
    public List<TodoDto.Repsonse> TodosTodoResponseDtos(List<Todo> todos) {
        if ( todos == null ) {
            return null;
        }

        List<TodoDto.Repsonse> list = new ArrayList<TodoDto.Repsonse>( todos.size() );
        for ( Todo todo : todos ) {
            list.add( TodoToTodoResponseDto( todo ) );
        }

        return list;
    }
}
