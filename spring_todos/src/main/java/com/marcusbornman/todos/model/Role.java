package com.marcusbornman.todos.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Entity
@Data
public class Role {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name;
}
