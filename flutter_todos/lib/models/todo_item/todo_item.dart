import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'todo_item.freezed.dart';
part 'todo_item.g.dart';

@freezed
abstract class TodoItem with _$TodoItem {
  factory TodoItem({
    String uuid,
    @required String title,
    String description,
  }) = _TodoItem;

  factory TodoItem.fromJson(Map<String, dynamic> json) =>
      _$TodoItemFromJson(json);

  static TodoItem fromDocument(DocumentSnapshot document) {
    return TodoItem(
      uuid: document.id,
      title: document.data()['title'],
      description: document.data()['description'],
    );
  }
}
