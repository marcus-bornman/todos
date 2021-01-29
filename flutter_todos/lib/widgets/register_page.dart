import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_todos/blocs/auth_bloc/auth_bloc.dart';
import 'package:flutter_todos/widgets/error_dialog.dart';
import 'package:flutter_todos/widgets/loading_page.dart';

class RegisterPage extends StatelessWidget {
  final _formKey = GlobalKey<FormState>();
  final _emailController = TextEditingController();
  final _passwordController = TextEditingController();
  final _confirmPasswordController = TextEditingController();

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
        if (state is Unauthenticated) return _buildRegisterPage(context, state);

        if (state is Loading) return LoadingPage();

        return null;
      },
    );
  }

  Scaffold _buildRegisterPage(BuildContext context, Unauthenticated state) {
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
                      validator: (input) {
                        RegExp emailRegex = RegExp(
                            r"^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,253}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,253}[a-zA-Z0-9])?)*$");
                        if (emailRegex.hasMatch(input)) return null;
                        return 'Invalid Email Address';
                      },
                    ),
                    TextFormField(
                      controller: _passwordController,
                      obscureText: true,
                      decoration: InputDecoration(
                        labelText: 'Password',
                        icon: Icon(Icons.lock),
                      ),
                      validator: (input) {
                        if (input.isNotEmpty && input.length >= 6) return null;

                        return 'Password must be at least 6 characters';
                      },
                    ),
                    TextFormField(
                      controller: _confirmPasswordController,
                      obscureText: true,
                      decoration: InputDecoration(
                        labelText: 'Confirm Password',
                        icon: Icon(Icons.spellcheck),
                      ),
                      validator: (input) {
                        if (input == _passwordController.value.text)
                          return null;

                        return 'Passwords do not match';
                      },
                    ),
                    Divider(),
                    SizedBox(
                      width: double.infinity,
                      child: RaisedButton(
                        color: Theme.of(context).primaryColor,
                        child: Text('Register'),
                        onPressed: () {
                          if (_formKey.currentState.validate()) {
                            BlocProvider.of<AuthBloc>(context).add(
                              Register(
                                _emailController.text,
                                _passwordController.text,
                              ),
                            );
                          }
                        },
                      ),
                    ),
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
