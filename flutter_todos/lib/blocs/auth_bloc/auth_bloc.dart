import 'dart:async';

import 'package:bloc/bloc.dart';
import 'package:equatable/equatable.dart';
import 'package:flutter_todos/exceptions/auth_exception.dart';
import 'package:flutter_todos/repos/user_repo/user_repo.dart';

part 'auth_event.dart';

part 'auth_state.dart';

class AuthBloc extends Bloc<AuthEvent, AuthState> {
  final UserRepo _userRepo;

  AuthBloc()
      : _userRepo = UserRepo.instance,
        super(Loading());

  @override
  Stream<AuthState> mapEventToState(AuthEvent event) async* {
    if (event is Initialize) {
      yield* _mapInitializeToState(event);
    }

    if (event is Register) {
      yield* _mapRegisterToState(event);
    }

    if (event is Login) {
      yield* _mapLoginToState(event);
    }

    if (event is Logout) {
      yield* _mapLogoutToState(event);
    }

    if (event is ChangeUser) {
      yield* _mapChangeUserToState(event);
    }
  }

  Stream<AuthState> _mapInitializeToState(Initialize event) async* {
    _userRepo.userIdStream().listen((userId) {
      add(ChangeUser(userId));
    });
  }

  Stream<AuthState> _mapRegisterToState(Register event) async* {
    yield Loading();

    try {
      await _userRepo.register(event.email, event.password);
    } on AuthException catch (e) {
      yield ErrorAuthenticating(e.reason);
      yield Unauthenticated();
    }
  }

  Stream<AuthState> _mapLoginToState(Login event) async* {
    yield Loading();

    try {
      await _userRepo.signIn(event.email, event.password);
    } on AuthException catch (e) {
      yield ErrorAuthenticating(e.reason);
      yield Unauthenticated();
    }
  }

  Stream<AuthState> _mapLogoutToState(Logout event) async* {
    yield Loading();

    await _userRepo.signOut();
  }

  Stream<AuthState> _mapChangeUserToState(ChangeUser event) async* {
    yield event.userId == null
        ? Unauthenticated()
        : Authenticated(event.userId);
  }
}
