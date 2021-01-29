import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_todos/blocs/auth_bloc/auth_bloc.dart';

class HomePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
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
              Text('Welcome Home'),
              Divider(),
              SizedBox(
                width: double.infinity,
                child: RaisedButton(
                  color: Theme.of(context).primaryColor,
                  child: Text('Sign Out'),
                  onPressed: () {
                    BlocProvider.of<AuthBloc>(context).add(Logout());
                  },
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
