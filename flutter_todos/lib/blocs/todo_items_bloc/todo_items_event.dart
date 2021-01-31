part of 'todo_items_bloc.dart';

abstract class TodoItemsEvent extends Equatable {
  const TodoItemsEvent();
}

class InitTodoItems extends TodoItemsEvent {
  @override
  List<Object> get props => [];
}

class AddTodoItem extends TodoItemsEvent {
  final TodoItem todoItem;

  AddTodoItem(this.todoItem);

  @override
  List<Object> get props => [todoItem];
}

class UpdateTodoItem extends TodoItemsEvent {
  final TodoItem todoItem;

  UpdateTodoItem(this.todoItem);

  @override
  List<Object> get props => [todoItem];
}

class DeleteTodoItem extends TodoItemsEvent {
  final TodoItem todoItem;

  DeleteTodoItem(this.todoItem);

  @override
  List<Object> get props => [todoItem];
}

class HandleItemChange extends TodoItemsEvent {
  final List<TodoItem> items;

  HandleItemChange(this.items);

  @override
  List<Object> get props => [items];
}
