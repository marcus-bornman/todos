package com.marcusbornman.spring_todos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "A user with the given username already exists")
public class UsernameExistsException extends Exception {
}
