import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_todos/blocs/auth_bloc/auth_bloc.dart';
import 'package:flutter_todos/widgets/landing_page.dart';

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp();

  runApp(TodosApp());
}

class TodosApp extends StatelessWidget {
  const TodosApp({
    Key key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return BlocProvider<AuthBloc>(
      create: (context) => AuthBloc()..add(Initialize()),
      child: MaterialApp(
        title: 'Todos',
        theme: ThemeData(primaryColor: Colors.lightGreen),
        home: LandingPage(),
      ),
    );
  }
}
