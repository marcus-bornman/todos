package com.marcusbornman.todos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No User was found with the given ID")
public class UserNotFoundException extends Exception {

}
