import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_todos/blocs/todo_items_bloc/todo_items_bloc.dart';
import 'package:flutter_todos/blocs/todo_lists_bloc/todo_lists_bloc.dart';
import 'package:flutter_todos/models/todo_list/todo_list.dart';
import 'package:flutter_todos/widgets/edit_list_page.dart';
import 'package:flutter_todos/widgets/todos_page.dart';

class TodoListTile extends StatelessWidget {
  const TodoListTile({
    Key key,
    @required this.todoList,
  }) : super(key: key);

  final TodoList todoList;

  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: Text(todoList.title),
      trailing: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          IconButton(
            icon: Icon(Icons.open_in_new),
            onPressed: () {
              Navigator.of(context).push(
                MaterialPageRoute(
                  builder: (context) {
                    return BlocProvider<TodoItemsBloc>(
                      create: (context) => TodoItemsBloc(todoList: todoList)
                        ..add(InitTodoItems()),
                      child: TodosPage(todoList: todoList),
                    );
                  },
                ),
              );
            },
          ),
          IconButton(
            icon: Icon(Icons.edit),
            onPressed: () async {
              TodoList editedList = await editTodoList(context, todoList);
              if (editedList != null) {
                BlocProvider.of<TodoListsBloc>(context)
                    .add(UpdateTodoList(editedList));
              }
            },
          ),
          IconButton(
            icon: Icon(Icons.delete),
            onPressed: () {
              BlocProvider.of<TodoListsBloc>(context)
                  .add(DeleteTodoList(todoList));
            },
          ),
        ],
      ),
    );
  }
}
