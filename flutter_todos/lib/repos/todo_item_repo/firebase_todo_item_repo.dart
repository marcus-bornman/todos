import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter_todos/models/todo_item/todo_item.dart';
import 'package:flutter_todos/models/todo_list/todo_list.dart';
import 'package:flutter_todos/repos/todo_item_repo/todo_item_repo.dart';

class FirebaseTodoItemRepo implements TodoItemRepo {
  final CollectionReference todoItemCollection;

  FirebaseTodoItemRepo(TodoList todoList)
      : todoItemCollection =
            FirebaseFirestore.instance.doc(todoList.uuid).collection('items');

  @override
  Future<void> addNewTodoItem(TodoItem todo) {
    return todoItemCollection.add(todo.toJson());
  }

  @override
  Future<void> deleteTodoItem(TodoItem todo) {
    return todoItemCollection.doc(todo.uuid).delete();
  }

  @override
  Stream<List<TodoItem>> todoItems() {
    return todoItemCollection.snapshots().map((snapshot) {
      return snapshot.docs.map((doc) => TodoItem.fromDocument(doc)).toList();
    });
  }

  @override
  Future<void> updateTodoItem(TodoItem todo) {
    return todoItemCollection.doc(todo.uuid).update(todo.toJson());
  }
}
