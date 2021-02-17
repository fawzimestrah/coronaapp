import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:firebase_database/firebase_database.dart';
import '../GlobalState.dart';
import '../main.dart';
import 'showFormDescriptionClient.dart';


final FirebaseAuth _auth = FirebaseAuth.instance;
class MainClient extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return new _MainClientState();
  }
}

class _MainClientState extends State<MainClient> {
  final databaseReference = FirebaseDatabase.instance.reference();
  DatabaseReference _firebaseRef = FirebaseDatabase().reference().child('FORMS');
  GlobalState _store= GlobalState.instance;
    bool _visible =false;
  List _litemsNameForm = [];
  List _litemsDateForm = [];
  List _litemsKeyForm = [];
  List _litemsDescForm = [];


  @override
  void initState() {
    super.initState();
    readMessage();
  }

  void updateList(index, data) {
    setState(() {
//      "key": index,
      _litemsNameForm.add(data["nameform"]);
      _litemsDateForm.add(data["dateform"]);
      _litemsKeyForm.add(data["key_value"]);
      _litemsDescForm.add(data["descformLang"]);
      //print("ds");
    });
  }

  void goToDescription(index){
    print("on tap!!!");
    _store.set("name",_litemsNameForm[index]);
    _store.set("dateform",_litemsNameForm[index]);
    _store.set("descformLang",_litemsDescForm[index]);
    _store.set("keyform",_litemsKeyForm[index]);
    print("toDesc ${_litemsKeyForm[index]}");
    Navigator.push(
        context,
        MaterialPageRoute(
          builder: (context) =>ShowFormDescClient(),

        ));
  }


  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        title: Text("NAS"),
        actions: <Widget>[
          new Row(
            children: <Widget>[
              new Visibility(
                maintainSize: true,
                maintainAnimation: true,
                maintainState: true,
                visible: _visible,
                child: new CircularProgressIndicator(
                  backgroundColor: Colors.white70,
                ),
              ),
              new InkWell(
                child:new Icon(Icons.logout) ,
              onTap: _signOut,
              ),
            ],
          )
        ],

      ),
      body: new Container(
        padding: EdgeInsets.only(top: 20),
        child: new Center(
          child: new Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Expanded(
                  child: ListView.builder(
                      itemCount: _litemsNameForm.length,
                      itemBuilder: (BuildContext ctxt, int index) {
                        return new GestureDetector(
                          onTap: () {
                            goToDescription(index);
                          },
                          child: new Center(
                            child: new Card(
                              color: Colors.white70,
                              margin: EdgeInsets.all(16),
                              child: new Row(
                                children: <Widget>[
                                  new Icon(Icons.assignment_turned_in,),
                                  new Container(
                                    padding: EdgeInsets.fromLTRB (10,20,10,20),
                                    child: new Column(
                                      children: <Widget>[
                                        new Text(
                                            '${_litemsNameForm[index]} ',
                                            style: new TextStyle(
                                                color: Colors.blueAccent, fontSize: 18)
                                        ),

                                        new Text(
                                            '${_litemsDateForm[index]} ',
                                            style: new TextStyle(
                                                color: Colors.blueAccent, fontSize: 18)
                                        ),

                                      ],
                                    )
                                    ,
                                  )
                                ],
                              ),
                            )
                          ),
                        );
                      }))
            ],
          ),
        )
      ),
    );
  }

  sendMessage() {
    _firebaseRef.push().set({
      "message": "asca",
      "timestamp": DateTime.now().millisecondsSinceEpoch
    });
  }

  readMessage() {

    _firebaseRef.child("FORMS").onValue;
    _firebaseRef.orderByPriority().onValue.listen((event) {

      var snapshot = event.snapshot;
 //   });// .then((DataSnapshot snapshot) {
      Map data = snapshot.value;
      _litemsNameForm = [];
      _litemsDateForm=[];
      _litemsKeyForm=[];
      _litemsDescForm=[];
      data.forEach((index, data) {
        updateList(index, data);
      });

//      print('Data : ${data['-MLJZdKf0oDkj7HJkTek']['descformLang']['AR']}');
      print('Data : $data');

      _store.set("ALLData", data);
      setState(() {
        _visible=false;
      });

    });
  }


  void _signOut() async {
    await _auth.signOut();

    Navigator.of(context).pushReplacement(MaterialPageRoute(
        builder: (context) =>new MyApp()));

  }
}

/*
final FirebaseUser user = await FirebaseAuth.instance.currentUser();
FirebaseDatabase.instance.reference().child("kullanıcılar").child(user.uid).child("takip").set({"uid":"uid"});
 */
