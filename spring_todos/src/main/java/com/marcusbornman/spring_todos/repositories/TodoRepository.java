package com.marcusbornman.spring_todos.repositories;

import com.marcusbornman.spring_todos.entities.Todo;
import com.marcusbornman.spring_todos.entities.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByTodoListUserUsernameAndTodoListId(String username, Long todoListId);

    Optional<Todo> findByTodoListUserUsernameAndTodoListIdAndId(String username, Long todoListId, Long id);
}
