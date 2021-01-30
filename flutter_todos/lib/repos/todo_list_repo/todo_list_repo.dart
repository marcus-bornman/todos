import 'package:flutter_todos/models/todo_list/todo_list.dart';

abstract class TodoListRepo {
  Future<void> addNewTodoList(TodoList todo);

  Future<void> deleteTodoList(TodoList todo);

  Stream<List<TodoList>> todoLists();

  Future<void> updateTodoList(TodoList todo);
}
