package com.soloproject.todoApp.todo.service;

import com.soloproject.todoApp.todo.entity.Todo;
import com.soloproject.todoApp.todo.repository.TodoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

/**
 * Mockito 적용하여 spring framework의 도움을 받지 않고 빠르게 진행
 */
@ExtendWith(MockitoExtension.class)  // Spring을 사용하지 않고 Jnuit에서 Mockito 기능 사용
public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks // @Mock해서 생성한 Mock 객체를 주입 (DI)
    private TodoService todoService;

    // 모르겠어.... 왜 오류가
    @Test
    @DisplayName("Todo 등록 테스트")
    public void createTodoTest() {
        // given
        Todo todo = new Todo("운동하기", 1, false);

        given(todoRepository.save(Mockito.any())).willReturn(todo);
        Todo createdTodo = todoService.createTodo(todo);

        // when/then
        Assertions.assertEquals("운동하기", createdTodo.getTitle());
    }

    @Test
    @DisplayName("등록된 전체 Todo 목록 조회")
    public void findTodosTest(){
        // given
        Todo todo1 = new Todo("운동하기", 1, false);
        todo1.setId(1L);

        Todo todo2 = new Todo("공부하기", 2, false);
        todo2.setId(2L);

        List<Todo> todos = new ArrayList<>();
        todos.add(todo1);
        todos.add(todo2);

        given(todoRepository.findAll()).willReturn(todos);

        // when/then
        List<Todo> findTodos = todoService.findTodos();

        Assertions.assertEquals(2,findTodos.size());
    }


    @Test
    @DisplayName("특정 id를 입력하여 조회")
    void findTodoTest() {

        // given
        Todo todo = new Todo("운동하기", 1, false);
        todo.setId(1L);

        given(todoRepository.findById(Mockito.anyLong())).willReturn(Optional.of(todo));

        // when / then
        Todo findTodo = todoService.findTodo(1L);

        Assertions.assertEquals(todo.getTitle(), findTodo.getTitle());
        Assertions.assertEquals(todo.getTodoOrder(), findTodo.getTodoOrder());

    }

}
