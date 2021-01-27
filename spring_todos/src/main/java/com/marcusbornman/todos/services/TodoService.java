package com.marcusbornman.todos.services;

import com.marcusbornman.todos.entities.Todo;
import com.marcusbornman.todos.entities.TodoList;
import com.marcusbornman.todos.exceptions.TodoListNotFoundException;
import com.marcusbornman.todos.exceptions.TodoNotFoundException;
import com.marcusbornman.todos.repositories.TodoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoService {

  private final TodoRepository todoRepository;

  private final TodoListService todoListService;

  /**
   * Save a new task.
   *
   * @param ownerUsername the username of the user to which the TodoList belongs.
   * @param todoListId    the ID of the TodoList to which the newTodo will belong.
   * @param newTodo       the new task entity to be created.
   * @return the created entity.
   * @throws TodoListNotFoundException if no TodoList could be found with the given ID, belonging to
   *                                   a user with the given username.
   */
  public Todo create(String ownerUsername, Long todoListId, Todo newTodo)
      throws TodoListNotFoundException {
    TodoList todoList = todoListService.read(ownerUsername, todoListId);
    newTodo.setTodoList(todoList);

    return todoRepository.save(newTodo);
  }

  /**
   * Get a saved task.
   *
   * @param ownerUsername the username of the user to which the TodoList belongs.
   * @param todoListId    the ID of the TodoList to which the task belongs.
   * @param todoId        the ID of the task.
   * @return the task with the given ID.
   * @throws TodoNotFoundException if no task could be found with the given ID, belonging to the
   *                               list with the given ID and user with the given username.
   */
  public Todo read(String ownerUsername, Long todoListId, Long todoId)
      throws TodoNotFoundException {
    return todoRepository
        .findByTodoListUserUsernameAndTodoListIdAndId(ownerUsername, todoListId, todoId)
        .orElseThrow(TodoNotFoundException::new);
  }

  /**
   * Get all saved tasks belonging to a specific list and user.
   *
   * @param ownerUsername the username of the user to which the TodoList belongs.
   * @param todoListId    the ID of the TodoList to which the tasks belong.
   * @return all Todos that belong to the TodoList with the given ID.
   */
  public List<Todo> readAll(String ownerUsername, Long todoListId) {
    return todoRepository.findByTodoListUserUsernameAndTodoListId(ownerUsername, todoListId);
  }

  /**
   * Update a saved task.
   *
   * @param ownerUsername the username of the user to which the TodoList belongs.
   * @param todoListId    the ID of the TodoList to which the task belongs.
   * @param todoId        the ID of the task.
   * @param updatedTodo   the task entity with the updated details.
   * @throws TodoNotFoundException if no task could be found with the given ID, belonging to the
   *                               list with the given ID and user with the given username.
   */
  public void update(String ownerUsername, Long todoListId, Long todoId, Todo updatedTodo)
      throws TodoNotFoundException {
    Todo currentTodo = read(ownerUsername, todoListId, todoId);

    updatedTodo.setId(currentTodo.getId());

    todoRepository.save(updatedTodo);
  }

  /**
   * Delete a saved task.
   *
   * @param ownerUsername the username of the user to which the TodoList belongs.
   * @param todoListId    the ID of the TodoList to which the task belongs.
   * @param todoId        the ID of the task.
   * @throws TodoNotFoundException if no task could be found with the given ID, belonging to the
   *                               list with the given ID and user with the given username.
   */
  public void delete(String ownerUsername, Long todoListId, Long todoId)
      throws TodoNotFoundException {
    Todo todo = read(ownerUsername, todoListId, todoId);

    todoRepository.delete(todo);
  }
}
