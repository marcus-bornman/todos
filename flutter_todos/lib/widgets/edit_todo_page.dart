import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_todos/models/todo_item/todo_item.dart';

Future<TodoItem> editTodo(BuildContext context, TodoItem todoList) async {
  TodoItem editedItem = await Navigator.of(context).push(
    MaterialPageRoute(builder: (context) {
      return EditTodoPage(todoItem: todoList);
    }),
  );

  return editedItem;
}

class EditTodoPage extends StatelessWidget {
  final _formKey;

  final TextEditingController _titleController;

  final TextEditingController _descriptionController;

  final TodoItem todoItem;

  EditTodoPage({Key key, @required this.todoItem})
      : _formKey = GlobalKey<FormState>(),
        _titleController = TextEditingController.fromValue(
            TextEditingValue(text: todoItem.title)),
        _descriptionController = TextEditingController.fromValue(
            TextEditingValue(text: todoItem.description ?? '')),
        super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(todoItem.uuid == null ? 'Add Todo' : 'Edit Todo'),
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
            ListTile(
              title: TextFormField(
                controller: _descriptionController,
                decoration: InputDecoration(labelText: 'Description'),
                minLines: 5,
                maxLines: 5,
              ),
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        child: Icon(Icons.save),
        onPressed: () {
          if (!_formKey.currentState.validate()) return;
          final newItem = todoItem.copyWith(
            title: _titleController.value.text,
            description: _descriptionController.value.text,
          );
          Navigator.of(context).pop(newItem);
        },
      ),
    );
  }
}
