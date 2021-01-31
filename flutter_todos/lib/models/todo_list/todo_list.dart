import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'todo_list.freezed.dart';
part 'todo_list.g.dart';

@freezed
abstract class TodoList with _$TodoList {
  factory TodoList({
    String uuid,
    @required String userUuid,
    @required String title,
  }) = _TodoList;

  factory TodoList.fromJson(Map<String, dynamic> json) =>
      _$TodoListFromJson(json);

  static TodoList fromDocument(DocumentSnapshot document) {
    return TodoList(
      uuid: document.id,
      userUuid: document.data()['userUuid'],
      title: document.data()['title'],
    );
  }
}
