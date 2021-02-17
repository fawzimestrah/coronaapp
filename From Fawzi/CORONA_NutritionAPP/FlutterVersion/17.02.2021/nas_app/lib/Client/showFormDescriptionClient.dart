import 'dart:collection';
import 'dart:math';

import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:nas_app/Client/showFormClient.dart';


import '../GlobalState.dart';

final FirebaseAuth _auth = FirebaseAuth.instance;
class ShowFormDescClient extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return new _StateshowFormDescClient();
  }
}

class _StateshowFormDescClient extends State<ShowFormDescClient> {
  GlobalState _store = GlobalState.instance;

String idUser;
  List<String> listLanguage = ['AR', 'EN'];
  String dropdownValueLanguage = "AR";
  Map _descForm;
  String _fieldDesc = "";

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    idUser = _auth.currentUser.uid;

    setState(() {
      _descForm = _store.get("descformLang");
      _fieldDesc = _descForm[dropdownValueLanguage];
    });
    print("dessss $_descForm");
  }

  void onClickNavigator(index) {
    switch (index) {
      case 0:
        if (Navigator.canPop(context)) {
          Navigator.pop(context);
        }
        break;

      case 1:
        Navigator.push(
          context,
          MaterialPageRoute(builder: (context) => ShowFormClient()),
        );
    }
  }

  @override
  Widget build(BuildContext context) {


    var cardLogo=   new Container(
      margin: EdgeInsets.fromLTRB(16, 4, 16, 4),
      color: Colors.white,
      child: Image.asset('assets/logosnasul.png'),
    );
    return new Scaffold(
        backgroundColor: Colors.white70,
        appBar: new AppBar(
          title: new Text(_store.get("name")),
        ),
        bottomNavigationBar: new BottomNavigationBar(
          items: [
            new BottomNavigationBarItem(
              icon: new Icon(Icons.cancel_rounded),
              title: new Text("Cancel"),
            ),
            new BottomNavigationBarItem(
                icon: new Icon(Icons.add_chart), title: new Text("Continue")),
          ],
          onTap: (int x) => {onClickNavigator(x)},
        ),
        body: new SingleChildScrollView(
          padding: EdgeInsets.all(16),
            child: new Column(
              children:<Widget> [
                new Card(
                  elevation: 20,
                  child: new SizedBox(
                    width: double.maxFinite,
                    child: new Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: <Widget>[
                        new Container(
                          margin: EdgeInsets.all(16),
                          child: new Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: <Widget>[
                              getDropDownLanguage(),
                              new Text(_fieldDesc,
                                  textDirection:
                                  dropdownValueLanguage=="AR"
                                      ? TextDirection.rtl
                                      : TextDirection.ltr),

                            ],
                          ),
                        )
                      ],
                    ),
                  ),
                ),
                cardLogo,
              ],
            )));
  }

  Widget getDropDownLanguage() {
    return new DropdownButton<String>(
      value: dropdownValueLanguage,
      icon: Icon(Icons.arrow_drop_down),
      iconSize: 24,
      elevation: 16,
      style: TextStyle(color: Colors.lightBlueAccent, fontSize: 18),
      underline: new Container(
        height: 2,
        color: Colors.lightBlueAccent,
      ),
      onChanged: (String data) {
        setState(() {
          dropdownValueLanguage = data;
          _fieldDesc = _descForm[dropdownValueLanguage];
        });
      },
      items: listLanguage.map<DropdownMenuItem<String>>((String value) {
        return DropdownMenuItem<String>(
          value: value,
          child: Text(value),
        );
      }).toList(),
    );
  }
}
