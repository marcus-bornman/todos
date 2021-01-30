import 'package:flutter_todos/models/todo_item/todo_item.dart';
import 'package:flutter_todos/models/todo_list/todo_list.dart';
import 'package:flutter_todos/repos/todo_item_repo/firebase_todo_item_repo.dart';

abstract class TodoItemRepo {
  factory TodoItemRepo.getInstance(TodoList todoList) =>
      FirebaseTodoItemRepo(todoList);

  Future<void> addNewTodoItem(TodoItem todo);

  Future<void> deleteTodoItem(TodoItem todo);

  Stream<List<TodoItem>> todoItems();

  Future<void> updateTodoItem(TodoItem todo);
}
