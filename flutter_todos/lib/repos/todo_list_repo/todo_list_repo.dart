import 'package:flutter_todos/models/todo_list/todo_list.dart';
import 'package:flutter_todos/repos/todo_list_repo/firebase_todo_list_repo.dart';

abstract class TodoListRepo {
  static TodoListRepo _instance;

  static get instance {
    if (_instance == null) _instance = FirebaseTodoListRepo();

    return _instance;
  }

  Future<void> addNewTodoList(TodoList todo);

  Future<void> deleteTodoList(TodoList todo);

  Stream<List<TodoList>> todoLists();

  Future<void> updateTodoList(TodoList todo);
}
