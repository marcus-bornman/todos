import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_todos/blocs/auth_bloc/auth_bloc.dart';
import 'package:flutter_todos/widgets/error_dialog.dart';
import 'package:flutter_todos/widgets/loading_page.dart';

class LoginPage extends StatelessWidget {
  final _formKey = GlobalKey<FormState>();
  final _emailController = TextEditingController();
  final _passwordController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return BlocConsumer<AuthBloc, AuthState>(
      listener: (context, state) {
        if (state is Authenticated) Navigator.of(context).pop();

        if (state is ErrorAuthenticating) {
          showErrorDialog(context, state.message);
        }
      },
      buildWhen: (prevState, nextState) {
        return nextState is Loading || nextState is Unauthenticated;
      },
      builder: (context, state) {
        if (state is Unauthenticated) return _buildLoginPage(context, state);

        if (state is Loading) return LoadingPage();

        return null;
      },
    );
  }

  Scaffold _buildLoginPage(BuildContext context, Unauthenticated state) {
    return Scaffold(
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Image(
                image: AssetImage('assets/project_logo.png'),
                width: 300.0,
              ),
              Divider(),
              Form(
                key: _formKey,
                child: Column(
                  children: [
                    TextFormField(
                      controller: _emailController,
                      decoration: InputDecoration(
                        labelText: 'Email',
                        icon: Icon(Icons.email),
                      ),
                    ),
                    TextFormField(
                      controller: _passwordController,
                      obscureText: true,
                      decoration: InputDecoration(
                        labelText: 'Password',
                        icon: Icon(Icons.lock),
                      ),
                    ),
                    Divider(),
                    SizedBox(
                      width: double.infinity,
                      child: RaisedButton(
                        color: Theme.of(context).primaryColor,
                        child: Text('Login'),
                        onPressed: () {
                          if (_formKey.currentState.validate()) {
                            BlocProvider.of<AuthBloc>(context).add(
                              Login(
                                _emailController.text,
                                _passwordController.text,
                              ),
                            );
                          }
                        },
                      ),
                    )
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
