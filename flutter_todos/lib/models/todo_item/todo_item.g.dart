// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'todo_item.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$_TodoItem _$_$_TodoItemFromJson(Map<String, dynamic> json) {
  return _$_TodoItem(
    uuid: json['uuid'] as String,
    title: json['title'] as String,
    description: json['description'] as String,
  );
}

Map<String, dynamic> _$_$_TodoItemToJson(_$_TodoItem instance) =>
    <String, dynamic>{
      'uuid': instance.uuid,
      'title': instance.title,
      'description': instance.description,
    };
