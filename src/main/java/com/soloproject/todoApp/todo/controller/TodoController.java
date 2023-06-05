package com.soloproject.todoApp.todo.controller;

import com.soloproject.todoApp.todo.dto.TodoDto;
import com.soloproject.todoApp.todo.entity.Todo;
import com.soloproject.todoApp.todo.mapper.TodoMapper;
import com.soloproject.todoApp.todo.service.TodoService;
import com.soloproject.todoApp.utils.UriCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@Validated
@RequestMapping("/")
public class TodoController {

    private final static String TODO_DEFAULT_URL = "/";

    private final TodoService todoService;
    private final TodoMapper mapper;

    public TodoController(TodoService todoService, TodoMapper mapper) {
        this.todoService = todoService;
        this.mapper = mapper;
    }

    /**
     * Post : 할일 목록 등록
     */
    @PostMapping
    public ResponseEntity postTodo(@RequestBody TodoDto.Post post){

        Todo todo = todoService.createTodo(mapper.TodoPostDtoToTodo(post));

        URI location = UriCreator.createUri(TODO_DEFAULT_URL, todo.getId());

        return ResponseEntity.created(location).build();
    }

    /**
     * Get : 전체 할 일 목록 조회
     */
    @GetMapping
    public ResponseEntity getTodos(){

        List<Todo> todos = todoService.findTodos();

        return new ResponseEntity<>(mapper.TodosTodoResponseDtos(todos), HttpStatus.OK);
    }

    /**
     * Get : 등록되어 있는 할 일의 특정 id를 입력하여 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity getTodo(@PathVariable("id") long id){

        Todo todo = todoService.findTodo(id);

        return new ResponseEntity<>(mapper.TodoToTodoResponseDto(todo), HttpStatus.OK);
    }

    /**
     * Patch : 등록되어 있는 할 일의 특정 id를 입력하여 수정
     */
    @PatchMapping("/{id}")
    public ResponseEntity patchTodo(@PathVariable("id") long id,
                                    @RequestBody TodoDto.Patch patch){

        patch.addId(id);
        Todo todo = todoService.updateTodo(mapper.TodoPatchDtoToTodo(patch));

        return new ResponseEntity<>(mapper.TodoToTodoResponseDto(todo), HttpStatus.OK);
    }

    /**
     * Delete : 등록된 전체 할 일 삭제
     */
    @DeleteMapping
    public ResponseEntity deleteTodos(){

        todoService.deleteTodos();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Delete : 할 일의 특정 id를 입력하여 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteTodo(@PathVariable("id") long id){

        todoService.deleteTodo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
