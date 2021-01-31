// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies

part of 'todo_item.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;
TodoItem _$TodoItemFromJson(Map<String, dynamic> json) {
  return _TodoItem.fromJson(json);
}

/// @nodoc
class _$TodoItemTearOff {
  const _$TodoItemTearOff();

// ignore: unused_element
  _TodoItem call({String uuid, @required String title, String description}) {
    return _TodoItem(
      uuid: uuid,
      title: title,
      description: description,
    );
  }

// ignore: unused_element
  TodoItem fromJson(Map<String, Object> json) {
    return TodoItem.fromJson(json);
  }
}

/// @nodoc
// ignore: unused_element
const $TodoItem = _$TodoItemTearOff();

/// @nodoc
mixin _$TodoItem {
  String get uuid;
  String get title;
  String get description;

  Map<String, dynamic> toJson();
  @JsonKey(ignore: true)
  $TodoItemCopyWith<TodoItem> get copyWith;
}

/// @nodoc
abstract class $TodoItemCopyWith<$Res> {
  factory $TodoItemCopyWith(TodoItem value, $Res Function(TodoItem) then) =
      _$TodoItemCopyWithImpl<$Res>;
  $Res call({String uuid, String title, String description});
}

/// @nodoc
class _$TodoItemCopyWithImpl<$Res> implements $TodoItemCopyWith<$Res> {
  _$TodoItemCopyWithImpl(this._value, this._then);

  final TodoItem _value;
  // ignore: unused_field
  final $Res Function(TodoItem) _then;

  @override
  $Res call({
    Object uuid = freezed,
    Object title = freezed,
    Object description = freezed,
  }) {
    return _then(_value.copyWith(
      uuid: uuid == freezed ? _value.uuid : uuid as String,
      title: title == freezed ? _value.title : title as String,
      description:
          description == freezed ? _value.description : description as String,
    ));
  }
}

/// @nodoc
abstract class _$TodoItemCopyWith<$Res> implements $TodoItemCopyWith<$Res> {
  factory _$TodoItemCopyWith(_TodoItem value, $Res Function(_TodoItem) then) =
      __$TodoItemCopyWithImpl<$Res>;
  @override
  $Res call({String uuid, String title, String description});
}

/// @nodoc
class __$TodoItemCopyWithImpl<$Res> extends _$TodoItemCopyWithImpl<$Res>
    implements _$TodoItemCopyWith<$Res> {
  __$TodoItemCopyWithImpl(_TodoItem _value, $Res Function(_TodoItem) _then)
      : super(_value, (v) => _then(v as _TodoItem));

  @override
  _TodoItem get _value => super._value as _TodoItem;

  @override
  $Res call({
    Object uuid = freezed,
    Object title = freezed,
    Object description = freezed,
  }) {
    return _then(_TodoItem(
      uuid: uuid == freezed ? _value.uuid : uuid as String,
      title: title == freezed ? _value.title : title as String,
      description:
          description == freezed ? _value.description : description as String,
    ));
  }
}

@JsonSerializable()

/// @nodoc
class _$_TodoItem implements _TodoItem {
  _$_TodoItem({this.uuid, @required this.title, this.description})
      : assert(title != null);

  factory _$_TodoItem.fromJson(Map<String, dynamic> json) =>
      _$_$_TodoItemFromJson(json);

  @override
  final String uuid;
  @override
  final String title;
  @override
  final String description;

  @override
  String toString() {
    return 'TodoItem(uuid: $uuid, title: $title, description: $description)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other is _TodoItem &&
            (identical(other.uuid, uuid) ||
                const DeepCollectionEquality().equals(other.uuid, uuid)) &&
            (identical(other.title, title) ||
                const DeepCollectionEquality().equals(other.title, title)) &&
            (identical(other.description, description) ||
                const DeepCollectionEquality()
                    .equals(other.description, description)));
  }

  @override
  int get hashCode =>
      runtimeType.hashCode ^
      const DeepCollectionEquality().hash(uuid) ^
      const DeepCollectionEquality().hash(title) ^
      const DeepCollectionEquality().hash(description);

  @JsonKey(ignore: true)
  @override
  _$TodoItemCopyWith<_TodoItem> get copyWith =>
      __$TodoItemCopyWithImpl<_TodoItem>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$_$_TodoItemToJson(this);
  }
}

abstract class _TodoItem implements TodoItem {
  factory _TodoItem({String uuid, @required String title, String description}) =
      _$_TodoItem;

  factory _TodoItem.fromJson(Map<String, dynamic> json) = _$_TodoItem.fromJson;

  @override
  String get uuid;
  @override
  String get title;
  @override
  String get description;
  @override
  @JsonKey(ignore: true)
  _$TodoItemCopyWith<_TodoItem> get copyWith;
}
