import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_todos/blocs/todo_items_bloc/todo_items_bloc.dart';
import 'package:flutter_todos/models/todo_item/todo_item.dart';
import 'package:flutter_todos/widgets/edit_todo_page.dart';

class TodoItemTile extends StatelessWidget {
  const TodoItemTile({
    Key key,
    @required this.todoItem,
  }) : super(key: key);

  final TodoItem todoItem;

  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: Text(todoItem.title),
      subtitle: Text(todoItem.description ?? ''),
      trailing: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          IconButton(
            icon: Icon(Icons.edit),
            onPressed: () async {
              TodoItem editedItem = await editTodo(context, todoItem);
              if (editedItem != null) {
                BlocProvider.of<TodoItemsBloc>(context)
                    .add(UpdateTodoItem(editedItem));
              }
            },
          ),
          IconButton(
            icon: Icon(Icons.check),
            onPressed: () {
              BlocProvider.of<TodoItemsBloc>(context)
                  .add(DeleteTodoItem(todoItem));
            },
          ),
        ],
      ),
    );
  }
}
