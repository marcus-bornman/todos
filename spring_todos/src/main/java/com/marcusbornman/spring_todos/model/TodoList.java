package com.marcusbornman.spring_todos.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
public class TodoList {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String title;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    @OneToMany(mappedBy = "todoList", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Todo> todos;
}
