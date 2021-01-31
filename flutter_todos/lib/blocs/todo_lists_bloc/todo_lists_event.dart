part of 'todo_lists_bloc.dart';

abstract class TodoListsEvent extends Equatable {
  const TodoListsEvent();
}

class InitTodoLists extends TodoListsEvent {
  final String userUuid;

  InitTodoLists(this.userUuid);

  @override
  List<Object> get props => [userUuid];
}

class AddTodoList extends TodoListsEvent {
  final TodoList todoList;

  AddTodoList(this.todoList);

  @override
  List<Object> get props => [todoList];
}

class UpdateTodoList extends TodoListsEvent {
  final TodoList todoList;

  UpdateTodoList(this.todoList);

  @override
  List<Object> get props => [todoList];
}

class DeleteTodoList extends TodoListsEvent {
  final TodoList todoList;

  DeleteTodoList(this.todoList);

  @override
  List<Object> get props => [todoList];
}

class HandleChange extends TodoListsEvent {
  final List<TodoList> lists;

  HandleChange(this.lists);

  @override
  List<Object> get props => [lists];
}
