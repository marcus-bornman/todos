package com.marcusbornman.spring_todos.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Role {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name;
}
