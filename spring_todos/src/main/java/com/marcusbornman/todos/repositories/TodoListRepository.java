package com.marcusbornman.todos.repositories;

import com.marcusbornman.todos.entities.TodoList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long> {

  List<TodoList> findByUserUsername(String username);

  Optional<TodoList> findByUserUsernameAndId(String username, Long id);
}
