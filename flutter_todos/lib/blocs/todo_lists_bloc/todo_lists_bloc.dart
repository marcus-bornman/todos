import 'dart:async';

import 'package:bloc/bloc.dart';
import 'package:equatable/equatable.dart';
import 'package:flutter_todos/models/todo_list/todo_list.dart';
import 'package:flutter_todos/repos/todo_list_repo/todo_list_repo.dart';

part 'todo_lists_event.dart';
part 'todo_lists_state.dart';

class TodoListsBloc extends Bloc<TodoListsEvent, TodoListsState> {
  final TodoListRepo _todoListRepo;
  StreamSubscription _todoListsSubscription;

  TodoListsBloc()
      : _todoListRepo = TodoListRepo.instance,
        super(TodoListsLoading());

  @override
  Stream<TodoListsState> mapEventToState(TodoListsEvent event) async* {
    if (event is InitTodoLists) {
      yield* _mapInitTodoListsToState(event);
    }

    if (event is AddTodoList) {
      yield* _mapAddTodoListToState(event);
    }

    if (event is UpdateTodoList) {
      yield* _mapUpdateTodoListToState(event);
    }

    if (event is DeleteTodoList) {
      yield* _mapDeleteTodoListToState(event);
    }

    if (event is HandleChange) {
      yield* _mapHandleChangeToState(event);
    }
  }

  Stream<TodoListsState> _mapInitTodoListsToState(InitTodoLists event) async* {
    _todoListsSubscription?.cancel();
    _todoListsSubscription =
        _todoListRepo.todoLists(event.userUuid).listen((lists) {
      add(HandleChange(lists));
    });
  }

  Stream<TodoListsState> _mapAddTodoListToState(AddTodoList event) async* {
    yield TodoListsLoading();
    _todoListRepo.addNewTodoList(event.todoList);
  }

  Stream<TodoListsState> _mapUpdateTodoListToState(
      UpdateTodoList event) async* {
    yield TodoListsLoading();
    _todoListRepo.updateTodoList(event.todoList);
  }

  Stream<TodoListsState> _mapDeleteTodoListToState(
      DeleteTodoList event) async* {
    yield TodoListsLoading();
    _todoListRepo.deleteTodoList(event.todoList);
  }

  Stream<TodoListsState> _mapHandleChangeToState(HandleChange event) async* {
    yield TodoListsLoaded(event.lists);
  }

  @override
  Future<void> close() {
    _todoListsSubscription?.cancel();
    return super.close();
  }
}
