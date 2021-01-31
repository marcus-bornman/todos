part of 'todo_items_bloc.dart';

abstract class TodoItemsState extends Equatable {
  const TodoItemsState();
}

class TodoItemsLoading extends TodoItemsState {
  @override
  List<Object> get props => [];
}

class TodoItemsLoaded extends TodoItemsState {
  final List<TodoItem> todoItems;

  TodoItemsLoaded(this.todoItems);

  @override
  List<Object> get props => [todoItems];
}
