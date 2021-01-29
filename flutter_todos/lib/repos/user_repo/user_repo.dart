import 'package:flutter_todos/exceptions/auth_exception.dart';
import 'package:flutter_todos/repos/user_repo/firebase_user_repo.dart';

abstract class UserRepo {
  static UserRepo _instance;

  static get instance {
    if (_instance == null) _instance = FirebaseUserRepo();

    return _instance;
  }

  /// Returns a stream of the currently authenticated user's ID. If there is no
  /// authenticated user, the stream will emit null.
  Stream<String> userIdStream();

  /// Creates a new user with the given email and password combination and signs
  /// that user in.
  ///
  /// May throw an [AuthException] if the registration was not successful. In
  /// this case [AuthException.reason] will contain the human-readable reason
  /// as to why the registration failed.
  Future<void> register(String email, String password);

  /// Signs in the user with the given email and password combination.
  ///
  /// May throw an [AuthException] if the sign in was not successful. In
  /// this case [AuthException.reason] will contain the human-readable reason
  /// as to why the sign in failed.
  Future<void> signIn(String email, String password);

  /// Signs out the currently signed in user if there is one.
  Future<void> signOut();
}