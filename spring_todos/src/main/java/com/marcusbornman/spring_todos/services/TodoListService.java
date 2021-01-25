package com.marcusbornman.spring_todos.services;

import com.marcusbornman.spring_todos.entities.TodoList;
import com.marcusbornman.spring_todos.entities.User;
import com.marcusbornman.spring_todos.exceptions.TodoListNotFoundException;
import com.marcusbornman.spring_todos.exceptions.UserNotFoundException;
import com.marcusbornman.spring_todos.repositories.TodoListRepository;
import com.marcusbornman.spring_todos.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoListService {
    private final TodoListRepository todoListRepository;

    private final UserService userService;

    public TodoList create(String ownerUsername, TodoList todoList) throws UserNotFoundException {
        User user = userService.read(ownerUsername);
        todoList.setUser(user);

        return todoListRepository.save(todoList);
    }

    public TodoList read(String ownerUsername, Long todoListId) throws TodoListNotFoundException {
        return todoListRepository.findByUserUsernameAndId(ownerUsername, todoListId)
                .orElseThrow(TodoListNotFoundException::new);
    }

    public List<TodoList> readAll(String ownerUsername) {
        return todoListRepository.findByUserUsername(ownerUsername);
    }

    public void update(String ownerUsername, Long todoListId, TodoList todoList) throws TodoListNotFoundException {
        TodoList currentTodoList = read(ownerUsername, todoListId);

        todoList.setId(currentTodoList.getId());

        todoListRepository.save(todoList);
    }

    public void delete(String ownerUsername, Long todoListId) throws TodoListNotFoundException {
        TodoList todoList = read(ownerUsername, todoListId);

        todoListRepository.delete(todoList);
    }
}
