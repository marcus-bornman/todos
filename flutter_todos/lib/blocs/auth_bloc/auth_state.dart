part of 'auth_bloc.dart';

abstract class AuthState extends Equatable {
  const AuthState();
}

class Unauthenticated extends AuthState {
  @override
  List<Object> get props => [];
}

class Loading extends AuthState {
  @override
  List<Object> get props => [];
}

class ErrorAuthenticating extends AuthState {
  final String message;

  ErrorAuthenticating(this.message);

  @override
  List<Object> get props => [message];
}

class Authenticated extends AuthState {
  final String userId;

  Authenticated(this.userId);

  @override
  List<Object> get props => [userId];
}