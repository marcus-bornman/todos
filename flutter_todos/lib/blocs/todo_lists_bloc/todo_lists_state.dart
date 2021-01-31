part of 'todo_lists_bloc.dart';

abstract class TodoListsState extends Equatable {
  const TodoListsState();
}

class Loading extends TodoListsState {
  @override
  List<Object> get props => [];
}

class Loaded extends TodoListsState {
  final List<TodoList> lists;

  Loaded(this.lists);

  @override
  List<Object> get props => [lists];
}
