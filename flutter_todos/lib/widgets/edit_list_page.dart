import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_todos/models/todo_list/todo_list.dart';

Future<TodoList> editTodoList(BuildContext context, TodoList todoList) async {
  TodoList editedList = await Navigator.of(context).push(
    MaterialPageRoute(builder: (context) {
      return EditListPage(todoList: todoList);
    }),
  );

  return editedList;
}

class EditListPage extends StatelessWidget {
  final _formKey;

  final TextEditingController _titleController;

  final TodoList todoList;

  EditListPage({Key key, @required this.todoList})
      : _formKey = GlobalKey<FormState>(),
        _titleController = TextEditingController.fromValue(
            TextEditingValue(text: todoList.title)),
        super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(todoList.uuid == null ? 'Add Todo List' : 'Edit Todo List'),
      ),
      body: Form(
        key: _formKey,
        child: ListView(
          children: [
            ListTile(
              title: TextFormField(
                controller: _titleController,
                decoration: InputDecoration(labelText: 'Title'),
                validator: (value) {
                  if (value.isEmpty) return 'Title cannot be blank.';

                  return null;
                },
              ),
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        child: Icon(Icons.save),
        onPressed: () {
          if (!_formKey.currentState.validate()) return;
          final newList = todoList.copyWith(title: _titleController.value.text);
          Navigator.of(context).pop(newList);
        },
      ),
    );
  }
}
