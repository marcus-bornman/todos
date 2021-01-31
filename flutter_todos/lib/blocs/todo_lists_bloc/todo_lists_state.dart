part of 'todo_lists_bloc.dart';

abstract class TodoListsState extends Equatable {
  const TodoListsState();
}

class TodoListsLoading extends TodoListsState {
  @override
  List<Object> get props => [];
}

class TodoListsLoaded extends TodoListsState {
  final List<TodoList> lists;

  TodoListsLoaded(this.lists);

  @override
  List<Object> get props => [lists];
}
