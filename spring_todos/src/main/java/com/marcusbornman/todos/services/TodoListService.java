package com.marcusbornman.todos.services;

import com.marcusbornman.todos.entities.TodoList;
import com.marcusbornman.todos.entities.User;
import com.marcusbornman.todos.exceptions.TodoListNotFoundException;
import com.marcusbornman.todos.exceptions.UserNotFoundException;
import com.marcusbornman.todos.repositories.TodoListRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoListService {

  private final TodoListRepository todoListRepository;

  private final UserService userService;

  /**
   * Save a new TodoList.
   *
   * @param ownerUsername the username of the owner of the new TodoList.
   * @param todoList the new TodoList entity to create.
   * @return the created TodoList entity.
   * @throws UserNotFoundException if no user could be found with the given username.
   */
  public TodoList create(String ownerUsername, TodoList todoList) throws UserNotFoundException {
    User user = userService.read(ownerUsername);
    todoList.setUser(user);

    return todoListRepository.save(todoList);
  }

  /**
   * Get a saved TodoList.
   *
   * @param ownerUsername the username of the owner of the TodoList to be read.
   * @param todoListId the ID pof the TodoList to be returned.
   * @return the created TodoList with the given ID.
   * @throws TodoListNotFoundException if no TodoList could be found with the given ID.
   */
  public TodoList read(String ownerUsername, Long todoListId) throws TodoListNotFoundException {
    return todoListRepository.findByUserUsernameAndId(ownerUsername, todoListId)
        .orElseThrow(TodoListNotFoundException::new);
  }

  /**
   * Get all saved TodoLists belonging to a specific user.
   *
   * @param ownerUsername the username of the user.
   * @return all TodoLists belonging to the user with the given username.
   */
  public List<TodoList> readAll(String ownerUsername) {
    return todoListRepository.findByUserUsername(ownerUsername);
  }

  /**
   * Update a saved TodoList.
   *
   * @param ownerUsername the username of the owner of the TodoList to update.
   * @param todoListId the ID of the TodoList to update.
   * @param todoList the entity with the updated details of the TodoList.
   * @throws TodoListNotFoundException if no TodoList could be found with the given ID.
   */
  public void update(String ownerUsername, Long todoListId, TodoList todoList)
      throws TodoListNotFoundException {
    TodoList currentTodoList = read(ownerUsername, todoListId);

    todoList.setId(currentTodoList.getId());

    todoListRepository.save(todoList);
  }

  /**
   * Delete a saved TodoList.
   *
   * @param ownerUsername the username of the owner of the TodoList.
   * @param todoListId the ID of the TodoList to delete.
   * @throws TodoListNotFoundException if no TodoList could be found with the given ID.
   */
  public void delete(String ownerUsername, Long todoListId) throws TodoListNotFoundException {
    TodoList todoList = read(ownerUsername, todoListId);

    todoListRepository.delete(todoList);
  }
}
