package com.marcusbornman.spring_todos.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Todo {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String title;

    @Nullable
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private TodoList todoList;
}
