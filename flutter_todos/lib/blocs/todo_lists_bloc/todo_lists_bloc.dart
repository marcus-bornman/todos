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
        super(Loading());

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

  _mapInitTodoListsToState(InitTodoLists event) async* {
    _todoListsSubscription?.cancel();
    _todoListRepo.todoLists(event.userUuid).listen(
          (lists) => add(HandleChange(lists)),
        );
  }

  _mapAddTodoListToState(AddTodoList event) async* {
    _todoListRepo.addNewTodoList(event.todoList);
  }

  _mapUpdateTodoListToState(UpdateTodoList event) async* {
    _todoListRepo.updateTodoList(event.todoList);
  }

  _mapDeleteTodoListToState(DeleteTodoList event) async* {
    _todoListRepo.deleteTodoList(event.todoList);
  }

  _mapHandleChangeToState(HandleChange event) async* {
    yield Loaded(event.lists);
  }

  @override
  Future<void> close() {
    _todoListsSubscription?.cancel();
    return super.close();
  }
}
