import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:nas_app/Client/showFormClient.dart';
import 'package:nas_app/resources/Languages.dart';

import 'GlobalState.dart';

class About extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return new _StateAbout();
  }
}

class _StateAbout extends State<About> {
  GlobalState _store = GlobalState.instance;

  List<String> listLanguage = ['AR', 'EN'];
  String dropdownValueLanguage = "AR";
  String _fieldDesc = "";
  int language = 0;

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    setState(() {
      _fieldDesc = Languages.About[language];
    });
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
    var cardText = new Card(
      elevation: 20,
      margin: EdgeInsets.fromLTRB(16, 4, 16, 4),
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
                  new Text(_fieldDesc,
                      textDirection: dropdownValueLanguage == "AR"
                          ? TextDirection.rtl
                          : TextDirection.ltr),
                ],
              ),
            )
          ],
        ),
      ),
    );

    var cardLogo=   new Container(
      margin: EdgeInsets.fromLTRB(16, 4, 16, 4),
      color: Colors.white,
      child: Image.asset('assets/logosnasul.png'),
    );

    return new Scaffold(
        backgroundColor: Colors.white70,
        appBar: new AppBar(
          title: new Text("About"),
        ),
        body: new SingleChildScrollView(
            child: new Column(
              children: <Widget>[
                new Container(
                  margin: EdgeInsets.fromLTRB(16, 4, 16, 4),
                  color: Colors.white,
                  child: getDropDownLanguage(),
                ),
                cardLogo,
                cardText,
              ],
            )));
  }

  Widget getDropDownLanguage() {
    return new DropdownButton<String>(
      value: dropdownValueLanguage,
      isExpanded: true,
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
          if (data == "AR") {
            language = 0;
          }
          if (data == "EN") {
            language = 1;
          }
          _fieldDesc = Languages.About[language];
        });
      },
      items: listLanguage.map<DropdownMenuItem<String>>((String value) {
        return DropdownMenuItem<String>(
          value: value,
          child: new Center(child:Text(value)),
        );
      }).toList(),
    );
  }
}
