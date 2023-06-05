package com.soloproject.todoApp.todo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mapstruct.Builder;

import javax.persistence.*;

@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "todos")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int todoOrder;

    @Column(nullable = false)
    private boolean completed = false;  // 할일 완료 여부 (기본 값:false)

    public Todo(String title, int todoOrder, boolean completed) {
        this.title = title;
        this.todoOrder = todoOrder;
        this.completed = completed;
    }
}
