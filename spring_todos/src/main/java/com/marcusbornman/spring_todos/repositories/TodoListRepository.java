package com.marcusbornman.spring_todos.repositories;

import com.marcusbornman.spring_todos.entities.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long> {
    List<TodoList> findByUserUsername(String username);

    Optional<TodoList> findByUserUsernameAndId(String username, Long id);
}
