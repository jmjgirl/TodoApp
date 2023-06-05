package com.soloproject.todoApp.todo.service;

import com.soloproject.todoApp.todo.entity.Todo;
import com.soloproject.todoApp.todo.repository.TodoRepository;
import com.soloproject.todoApp.utils.CustomBeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final CustomBeanUtils<Todo> beanUtils;

    /**
     * Create : 할일 목록 등록
     */
    public Todo createTodo(Todo todo){
        return todoRepository.save(todo);
    }

    /**
     * Read : 등록된 전체 할 일 목록 조회
     */
    public List<Todo> findTodos(){
        return todoRepository.findAll();
    }

    /**
     * Read : 등록되어 있는 할 일의 특정 id를 입력하여 조회
     */
    public Todo findTodo(long id){
        // todo가 있는지 조회한 후 있으면 반환 / 없으면 예외 발생
        Todo todo = findVerifiedTodo(id);
        return todo;
    }

    /**
     * Update : 할 일을 수정
     * - 내용 수정 가능
     * - 이미 한 일에는 완료 표시
     */
    public Todo updateTodo(Todo todo){
        // todo가 있는지 조회한 후 있으면 반환 / 없으면 예외 발생
        Todo findTodo = findVerifiedTodo(todo.getId());

        return beanUtils.copyNonNullProperties(todo,findTodo);
    }

    /**
     * Delete : 등록된 전체 할 일 삭제
     */
    public void deleteTodos(){

        todoRepository.deleteAll();
    }

    /**
     * Delete : 할 일의 특정 id를 입력하여 삭제
     */
    public void deleteTodo(long id){

        todoRepository.deleteById(id);
    }


    /**
     * id로 Todo가 있는지 조회
     * 없다면 exception 발생
     */
    public Todo findVerifiedTodo(long id){
        Optional<Todo> optionalTodo = todoRepository.findById(id);

        Todo findTodo = optionalTodo.orElseThrow(() ->
                new RuntimeException("조회하신 Todo가 존재하지 않습니다"));

        return findTodo;
    }
}
