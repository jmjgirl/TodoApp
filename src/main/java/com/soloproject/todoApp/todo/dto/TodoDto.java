package com.soloproject.todoApp.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;


public class TodoDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Post{
        private String title;

        private int todoOrder;

        private boolean completed;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Patch{
        private long id;
        private String title;

        private int todoOrder;

        private boolean completed;

        public void addId(long id){
            this.id = id;
        }

        public Patch(String title, int todoOrder, boolean completed) {
            this.title = title;
            this.todoOrder = todoOrder;
            this.completed = completed;
        }
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Repsonse {
        private long id;

        private String title;

        private int todoOrder;

        private boolean completed;
    }
}
