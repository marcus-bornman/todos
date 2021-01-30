// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies

part of 'todo_list.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;
TodoList _$TodoListFromJson(Map<String, dynamic> json) {
  return _TodoList.fromJson(json);
}

/// @nodoc
class _$TodoListTearOff {
  const _$TodoListTearOff();

// ignore: unused_element
  _TodoList call({@required String title, @required List<TodoItem> items}) {
    return _TodoList(
      title: title,
      items: items,
    );
  }

// ignore: unused_element
  TodoList fromJson(Map<String, Object> json) {
    return TodoList.fromJson(json);
  }
}

/// @nodoc
// ignore: unused_element
const $TodoList = _$TodoListTearOff();

/// @nodoc
mixin _$TodoList {
  String get title;
  List<TodoItem> get items;

  Map<String, dynamic> toJson();
  @JsonKey(ignore: true)
  $TodoListCopyWith<TodoList> get copyWith;
}

/// @nodoc
abstract class $TodoListCopyWith<$Res> {
  factory $TodoListCopyWith(TodoList value, $Res Function(TodoList) then) =
      _$TodoListCopyWithImpl<$Res>;
  $Res call({String title, List<TodoItem> items});
}

/// @nodoc
class _$TodoListCopyWithImpl<$Res> implements $TodoListCopyWith<$Res> {
  _$TodoListCopyWithImpl(this._value, this._then);

  final TodoList _value;
  // ignore: unused_field
  final $Res Function(TodoList) _then;

  @override
  $Res call({
    Object title = freezed,
    Object items = freezed,
  }) {
    return _then(_value.copyWith(
      title: title == freezed ? _value.title : title as String,
      items: items == freezed ? _value.items : items as List<TodoItem>,
    ));
  }
}

/// @nodoc
abstract class _$TodoListCopyWith<$Res> implements $TodoListCopyWith<$Res> {
  factory _$TodoListCopyWith(_TodoList value, $Res Function(_TodoList) then) =
      __$TodoListCopyWithImpl<$Res>;
  @override
  $Res call({String title, List<TodoItem> items});
}

/// @nodoc
class __$TodoListCopyWithImpl<$Res> extends _$TodoListCopyWithImpl<$Res>
    implements _$TodoListCopyWith<$Res> {
  __$TodoListCopyWithImpl(_TodoList _value, $Res Function(_TodoList) _then)
      : super(_value, (v) => _then(v as _TodoList));

  @override
  _TodoList get _value => super._value as _TodoList;

  @override
  $Res call({
    Object title = freezed,
    Object items = freezed,
  }) {
    return _then(_TodoList(
      title: title == freezed ? _value.title : title as String,
      items: items == freezed ? _value.items : items as List<TodoItem>,
    ));
  }
}

@JsonSerializable()

/// @nodoc
class _$_TodoList implements _TodoList {
  _$_TodoList({@required this.title, @required this.items})
      : assert(title != null),
        assert(items != null);

  factory _$_TodoList.fromJson(Map<String, dynamic> json) =>
      _$_$_TodoListFromJson(json);

  @override
  final String title;
  @override
  final List<TodoItem> items;

  @override
  String toString() {
    return 'TodoList(title: $title, items: $items)';
  }

  @override
  bool operator ==(dynamic other) {
    return identical(this, other) ||
        (other is _TodoList &&
            (identical(other.title, title) ||
                const DeepCollectionEquality().equals(other.title, title)) &&
            (identical(other.items, items) ||
                const DeepCollectionEquality().equals(other.items, items)));
  }

  @override
  int get hashCode =>
      runtimeType.hashCode ^
      const DeepCollectionEquality().hash(title) ^
      const DeepCollectionEquality().hash(items);

  @JsonKey(ignore: true)
  @override
  _$TodoListCopyWith<_TodoList> get copyWith =>
      __$TodoListCopyWithImpl<_TodoList>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$_$_TodoListToJson(this);
  }
}

abstract class _TodoList implements TodoList {
  factory _TodoList({@required String title, @required List<TodoItem> items}) =
      _$_TodoList;

  factory _TodoList.fromJson(Map<String, dynamic> json) = _$_TodoList.fromJson;

  @override
  String get title;
  @override
  List<TodoItem> get items;
  @override
  @JsonKey(ignore: true)
  _$TodoListCopyWith<_TodoList> get copyWith;
}
