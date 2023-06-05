package com.soloproject.todoApp.todo.repository;

import com.soloproject.todoApp.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo,Long> {
}
