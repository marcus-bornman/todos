import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter_todos/models/todo_list/todo_list.dart';
import 'package:flutter_todos/repos/todo_list_repo/todo_list_repo.dart';

class FirebaseTodoListRepo implements TodoListRepo {
  final todoListCollection = FirebaseFirestore.instance.collection('todoLists');

  @override
  Future<void> addNewTodoList(TodoList todoList) {
    return todoListCollection.add(todoList.toJson());
  }

  @override
  Future<void> deleteTodoList(TodoList todoList) {
    return todoListCollection.doc(todoList.uuid).delete();
  }

  @override
  Stream<List<TodoList>> todoLists() {
    return todoListCollection.snapshots().map((snapshot) {
      return snapshot.docs.map((doc) => TodoList.fromJson(doc.data())).toList();
    });
  }

  @override
  Future<void> updateTodoList(TodoList todoList) {
    return todoListCollection.doc(todoList.uuid).update(todoList.toJson());
  }
}
