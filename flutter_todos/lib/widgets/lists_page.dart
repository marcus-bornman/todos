import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_todos/blocs/todo_lists_bloc/todo_lists_bloc.dart';
import 'package:flutter_todos/models/todo_list/todo_list.dart';
import 'package:flutter_todos/widgets/edit_list_page.dart';

class ListsPage extends StatelessWidget {
  final String userUuid;

  const ListsPage({Key key, @required this.userUuid}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Todo Lists')),
      body: BlocBuilder<TodoListsBloc, TodoListsState>(
        builder: (context, state) {
          if (state is TodoListsLoading) return LinearProgressIndicator();

          if (state is TodoListsLoaded) {
            return ListView.separated(
              itemCount: state.lists.length,
              separatorBuilder: (context, index) => Divider(),
              itemBuilder: (context, index) {
                final todoList = state.lists.elementAt(index);
                return ListTile(
                  title: Text(todoList.title),
                  trailing: Row(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      IconButton(
                        icon: Icon(Icons.open_in_new),
                        onPressed: () {
                          BlocProvider.of<TodoListsBloc>(context)
                              .add(DeleteTodoList(todoList));
                        },
                      ),
                      IconButton(
                        icon: Icon(Icons.edit),
                        onPressed: () async {
                          TodoList editedList =
                              await editTodoList(context, todoList);
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
              },
            );
          }

          return null;
        },
      ),
      floatingActionButton: FloatingActionButton(
        child: Icon(Icons.add),
        onPressed: () async {
          TodoList todoList = TodoList(userUuid: userUuid, title: '');
          TodoList newTodoList = await editTodoList(context, todoList);

          if (newTodoList != null) {
            BlocProvider.of<TodoListsBloc>(context)
                .add(AddTodoList(newTodoList));
          }
        },
      ),
    );
  }
}
