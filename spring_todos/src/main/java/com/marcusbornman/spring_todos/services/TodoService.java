package com.marcusbornman.spring_todos.services;

import com.marcusbornman.spring_todos.entities.Todo;
import com.marcusbornman.spring_todos.entities.TodoList;
import com.marcusbornman.spring_todos.entities.User;
import com.marcusbornman.spring_todos.exceptions.TodoListNotFoundException;
import com.marcusbornman.spring_todos.exceptions.TodoNotFoundException;
import com.marcusbornman.spring_todos.repositories.TodoListRepository;
import com.marcusbornman.spring_todos.repositories.TodoRepository;
import com.marcusbornman.spring_todos.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;

    private final TodoListService todoListService;

    public Todo create(String ownerUsername, Long todoListId, Todo todo) throws TodoListNotFoundException {
        TodoList todoList = todoListService.read(ownerUsername, todoListId);
        todo.setTodoList(todoList);

        return todoRepository.save(todo);
    }

    public Todo read(String ownerUsername, Long todoListId, Long todoId) throws TodoNotFoundException {
        return todoRepository.findByTodoListUserUsernameAndTodoListIdAndId(ownerUsername, todoListId, todoId)
                .orElseThrow(TodoNotFoundException::new);
    }

    public List<Todo> readAll(String ownerUsername, Long todoListId) {
        return todoRepository.findByTodoListUserUsernameAndTodoListId(ownerUsername, todoListId);
    }

    public Todo update(String ownerUsername, Long todoListId, Long todoId, Todo todo) throws TodoNotFoundException {
        Todo currentTodo = read(ownerUsername, todoListId, todoId);

        todo.setId(currentTodo.getId());

        return todoRepository.save(todo);
    }

    public void delete(String ownerUsername, Long todoListId, Long todoId) throws TodoNotFoundException {
        Todo todo = read(ownerUsername, todoListId, todoId);

        todoRepository.delete(todo);
    }
}
