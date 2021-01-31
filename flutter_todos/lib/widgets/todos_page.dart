import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_todos/blocs/todo_items_bloc/todo_items_bloc.dart';
import 'package:flutter_todos/models/todo_item/todo_item.dart';
import 'package:flutter_todos/models/todo_list/todo_list.dart';
import 'package:flutter_todos/widgets/edit_todo_page.dart';
import 'package:flutter_todos/widgets/todo_item_tile.dart';

class TodosPage extends StatelessWidget {
  final TodoList todoList;

  const TodosPage({Key key, @required this.todoList}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(todoList.title)),
      body: BlocBuilder<TodoItemsBloc, TodoItemsState>(
        builder: (context, state) {
          if (state is TodoItemsLoading) return LinearProgressIndicator();

          if (state is TodoItemsLoaded) {
            return ListView.separated(
              itemCount: state.todoItems.length,
              separatorBuilder: (context, index) => Divider(),
              itemBuilder: (context, index) {
                final todoItem = state.todoItems.elementAt(index);

                return TodoItemTile(todoItem: todoItem);
              },
            );
          }

          return null;
        },
      ),
      floatingActionButton: FloatingActionButton(
        child: Icon(Icons.add),
        onPressed: () async {
          TodoItem todoItem = TodoItem(
            title: '',
            description: '',
          );
          TodoItem newTodoItem = await editTodo(context, todoItem);

          if (newTodoItem != null) {
            BlocProvider.of<TodoItemsBloc>(context)
                .add(AddTodoItem(newTodoItem));
          }
        },
      ),
    );
  }
}
