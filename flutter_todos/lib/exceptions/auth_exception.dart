class AuthException implements Exception {
  final String reason;

  const AuthException(this.reason);
}
