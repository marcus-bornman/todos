import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter_todos/exceptions/auth_exception.dart';
import 'package:flutter_todos/repos/user_repo/user_repo.dart';

class FirebaseUserRepo implements UserRepo {
  final FirebaseAuth _firebaseAuth;

  FirebaseUserRepo() : _firebaseAuth = FirebaseAuth.instance;

  @override
  Stream<String> userIdStream() {
    return _firebaseAuth.authStateChanges().asyncMap<String>((user) {
      return user == null ? null : user.uid;
    });
  }

  @override
  Future<void> register(String email, String password) async {
    try {
      await FirebaseAuth.instance.createUserWithEmailAndPassword(
        email: email,
        password: password,
      );
    } on FirebaseAuthException catch (e) {
      throw AuthException(e.message);
    } catch (e) {
      throw AuthException(
          'Could not complete registration. An unexpected error occurred.');
    }
  }

  @override
  Future<void> signIn(String email, String password) async {
    try {
      await FirebaseAuth.instance.signInWithEmailAndPassword(
        email: email,
        password: password,
      );
    } on FirebaseAuthException catch (e) {
      throw AuthException(e.message);
    } catch (e) {
      throw AuthException(
          'Could not sign in. An unexpected error occurred.');
    }
  }

  @override
  Future<void> signOut() async {
    await _firebaseAuth.signOut();
  }
}
