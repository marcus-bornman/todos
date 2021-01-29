part of 'auth_bloc.dart';

abstract class AuthEvent extends Equatable {
  const AuthEvent();
}

class Initialize extends AuthEvent {
  @override
  List<Object> get props => [];
}

class Register extends AuthEvent {
  final String email;

  final String password;

  Register(this.email, this.password);

  @override
  List<Object> get props => [email, password];
}

class Login extends AuthEvent {
  final String email;

  final String password;

  Login(this.email, this.password);

  @override
  List<Object> get props => [email, password];
}

class Logout extends AuthEvent {
  @override
  List<Object> get props => [];
}

class ChangeUser extends AuthEvent {
  final String userId;

  ChangeUser(this.userId);

  @override
  List<Object> get props => [userId];
}
