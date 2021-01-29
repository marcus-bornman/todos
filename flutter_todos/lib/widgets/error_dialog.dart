import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

showErrorDialog(BuildContext context, String message) async {
  showDialog(
    context: context,
    builder: (context) {
      return ErrorDialog(message: message);
    },
  );
}

class ErrorDialog extends StatelessWidget {
  final String message;

  const ErrorDialog({Key key, @required this.message}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text('Oops, something went wrong!', textAlign: TextAlign.center),
      content: Text(message, textAlign: TextAlign.center),
      actions: [
        OutlineButton(
          child: Text('OK'),
          onPressed: () => Navigator.of(context).pop(),
        ),
      ],
    );
  }
}
