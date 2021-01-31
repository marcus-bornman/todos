import 'package:flutter_todos/models/todo_list/todo_list.dart';
import 'package:flutter_todos/repos/todo_list_repo/firebase_todo_list_repo.dart';

abstract class TodoListRepo {
  static TodoListRepo _instance;

  static get instance {
    if (_instance == null) _instance = FirebaseTodoListRepo();

    return _instance;
  }

  /// Adds a new TodoList to persistent storage.
  Future<void> addNewTodoList(TodoList todo);

  /// Deletes a TodoList from persistent storage.
  Future<void> deleteTodoList(TodoList todo);

  /// Returns a streamed list of all TodoLists for a specific user.
  Stream<List<TodoList>> todoLists(String userUuid);

  /// Updates a TodoList in persistent storage.
  Future<void> updateTodoList(TodoList todo);
}
