// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'todo_list.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$_TodoList _$_$_TodoListFromJson(Map<String, dynamic> json) {
  return _$_TodoList(
    uuid: json['uuid'] as String,
    userUuid: json['userUuid'] as String,
    title: json['title'] as String,
    items: (json['items'] as List)
        ?.map((e) =>
            e == null ? null : TodoItem.fromJson(e as Map<String, dynamic>))
        ?.toList(),
  );
}

Map<String, dynamic> _$_$_TodoListToJson(_$_TodoList instance) =>
    <String, dynamic>{
      'uuid': instance.uuid,
      'userUuid': instance.userUuid,
      'title': instance.title,
      'items': instance.items,
    };
