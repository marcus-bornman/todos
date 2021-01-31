import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_todos/blocs/auth_bloc/auth_bloc.dart';
import 'package:flutter_todos/widgets/auth_page.dart';
import 'package:flutter_todos/widgets/lists_page.dart';
import 'package:flutter_todos/widgets/loading_page.dart';

class LandingPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return BlocBuilder<AuthBloc, AuthState>(
      buildWhen: (prevState, nextState) {
        return (nextState is Loading ||
            nextState is Unauthenticated ||
            nextState is Authenticated);
      },
      builder: (context, state) {
        if (state is Loading) return LoadingPage();

        if (state is Unauthenticated) return AuthPage();

        if (state is Authenticated) return ListsPage();

        return null;
      },
    );
  }
}
