package com.marcusbornman.todos.repositories;

import com.marcusbornman.todos.entities.Todo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

  List<Todo> findByTodoListUserUsernameAndTodoListId(String username, Long todoListId);

  Optional<Todo> findByTodoListUserUsernameAndTodoListIdAndId(String username, Long todoListId,
      Long id);
}
