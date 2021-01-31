import 'dart:async';

import 'package:bloc/bloc.dart';
import 'package:equatable/equatable.dart';
import 'package:flutter/material.dart';
import 'package:flutter_todos/models/todo_item/todo_item.dart';
import 'package:flutter_todos/models/todo_list/todo_list.dart';
import 'package:flutter_todos/repos/todo_item_repo/todo_item_repo.dart';

part 'todo_items_event.dart';
part 'todo_items_state.dart';

class TodoItemsBloc extends Bloc<TodoItemsEvent, TodoItemsState> {
  final TodoItemRepo _todoItemRepo;
  StreamSubscription _todoItemsSubscription;

  TodoItemsBloc({@required TodoList todoList})
      : _todoItemRepo = TodoItemRepo.getInstance(todoList),
        super(TodoItemsLoading());

  @override
  Stream<TodoItemsState> mapEventToState(
    TodoItemsEvent event,
  ) async* {
    if (event is InitTodoItems) {
      yield* _mapInitTodoItemsToState(event);
    }

    if (event is AddTodoItem) {
      yield* _mapAddTodoItemToState(event);
    }

    if (event is UpdateTodoItem) {
      yield* _mapUpdateTodoItemToState(event);
    }

    if (event is DeleteTodoItem) {
      yield* _mapDeleteTodoItemToState(event);
    }

    if (event is HandleItemChange) {
      yield* _mapHandleItemChangeToState(event);
    }
  }

  Stream<TodoItemsState> _mapInitTodoItemsToState(InitTodoItems event) async* {
    _todoItemsSubscription?.cancel();
    _todoItemsSubscription = _todoItemRepo.todoItems().listen((lists) {
      add(HandleItemChange(lists));
    });
  }

  Stream<TodoItemsState> _mapAddTodoItemToState(AddTodoItem event) async* {
    yield TodoItemsLoading();
    _todoItemRepo.addNewTodoItem(event.todoItem);
  }

  Stream<TodoItemsState> _mapUpdateTodoItemToState(
      UpdateTodoItem event) async* {
    yield TodoItemsLoading();
    _todoItemRepo.updateTodoItem(event.todoItem);
  }

  Stream<TodoItemsState> _mapDeleteTodoItemToState(
      DeleteTodoItem event) async* {
    yield TodoItemsLoading();
    _todoItemRepo.deleteTodoItem(event.todoItem);
  }

  Stream<TodoItemsState> _mapHandleItemChangeToState(
      HandleItemChange event) async* {
    yield TodoItemsLoaded(event.items);
  }

  @override
  Future<void> close() {
    _todoItemsSubscription?.cancel();
    return super.close();
  }
}
