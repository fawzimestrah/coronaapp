import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_database/firebase_database.dart';
import 'package:flutter/material.dart';
import 'package:nas_app/DataAnswers.dart';
import 'package:nas_app/DataQuestions.dart';
import 'package:nas_app/resources/Languages.dart';
import '../GlobalState.dart';

final FirebaseAuth _auth = FirebaseAuth.instance;

class ShowFormClient extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return new StateShowFormClient();
  }
}

class StateShowFormClient extends State<ShowFormClient> {
  final databaseReference = FirebaseDatabase.instance.reference();
  final TextEditingController _textFAnswersController = TextEditingController();
  final GlobalKey<ScaffoldState> scaffoldState = new GlobalKey<ScaffoldState>();

  String userId = "";

  GlobalState _store = GlobalState.instance;
  List listTest = ["fawzi1"];
  String _keyForm = "";
  String Question = "";
  List<DataQuestions> listQuestion = [];
  List<DataAnswers> listReponse = [];

  List<String> listLanguage = ['AR', 'EN'];
  String dropdownValueLanguage = "AR";
  int index = 0;
  bool _visibleProgress = false,
      _visibileTextField=true,
      _visibleRadioButton = false,
      _visibleCheckBox = true,
      _enableAnswers = false,
      _visibleSpinner = false,
      _finish = false;
  int radioGroup;
  List checkboxGroup = [];
  List<String> listChoiceSpinner = [];
  String dropdownValueSpinnerChoice;

  @override
  void initState() {
    super.initState();
    userId = _auth.currentUser.uid;

    setState(() {
      _keyForm = _store.get("keyform").toString();
    });

    readMessage();
  }

  void radioEvent(int value) {
    setState(() {
      radioGroup = value;
      print("radio = $radioGroup");
      _textFAnswersController.text = listQuestion[index].choix[value];
    });
  }

  void checkBoxEvent(int indexCh, bool value) {
    setState(() {
      checkboxGroup[indexCh] = value;
      print("value ${checkboxGroup[indexCh]}");
      if (value) {
        _textFAnswersController.text +=
            "${listQuestion[index].choix[indexCh]} ; ";
      } else {
        print(
            "${listQuestion[index].choix[indexCh]}  //  ${_textFAnswersController.text}");
        _textFAnswersController.text = _textFAnswersController.text
            .replaceAll("${listQuestion[index].choix[indexCh]} ; ", "");
      }
    });
  }

  void setQuestion(pos) {
    if (pos > listQuestion.length - 1) {
      print("finish");
      setState(() {
        _finish = true;
        Question = listQuestion[pos].questions[dropdownValueLanguage];
        _textFAnswersController.text = listReponse[pos].answer;
      });
    } else {
      setState(() {
        Question = listQuestion[pos].questions[dropdownValueLanguage];
        _textFAnswersController.text = listReponse[pos].answer;
      });
      //if(_textFAnswersController.text==(listQuestion[index].choix[indexRadio])){
    }
  }

  //}

  void onclick() {
    setState(() {});
  }

  void onClickNavigation(int ind) {
    if(_finish){
      return ;
    }
    switch (ind) {
      case 0:
        if (index > 0) {
          radioGroup = -1;
          print("previous");

          //          if (_textFAnswersController.text != "") {
//            listReponse[index].answer = _textFAnswersController.text;
//            sendMessage();
//          }
          index--;
          _textFAnswersController.text = listReponse[index].answer;
          testWithType();
        }
        break;
      case 1:
        print("navig=$ind");
        if (index < listQuestion.length) {
          radioGroup = -1;
          print("next");

          listReponse[index].answer = _textFAnswersController.text;
          if (_textFAnswersController.text != "") {
            sendMessage();

            if (index == listQuestion.length - 1) {
              finishFrom(); // + validation
              return;
            }

            if (index < listQuestion.length - 1) {
              index++;
            }

            if (index < listQuestion.length) {
              print(" Button next index=$index");
              _textFAnswersController.text = listReponse[index].answer;
              testWithType();
            }
          } else {
            SnackDialog("Empty Text");
          }
        }
        break;
    }
    setQuestion(index);
  }

  Widget getDropDownLanguage() {
    return new DropdownButton<String>(
      isExpanded: true,
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
          setQuestion(index);
        });
      },
      items: listLanguage.map<DropdownMenuItem<String>>((String value) {
        return DropdownMenuItem<String>(
            value: value,
            child: new Center(
              child: new Text(
                value,
                textAlign: TextAlign.center,
              ),
            ));
      }).toList(),
    );
  }

  @override
  Widget build(BuildContext context) {
    var questionField = new Container(
      child: Center(
          child: Container(
              padding: EdgeInsets.fromLTRB(20, 30, 30, 50),
              child: Text("Question ${index + 1} :\n$Question",
                  textDirection: dropdownValueLanguage == "AR"
                      ? TextDirection.rtl
                      : TextDirection.ltr,
                  style: TextStyle(fontSize: 22)))),
    );
    var cardRadioButton = ListView.builder(
        physics: NeverScrollableScrollPhysics(),
        shrinkWrap: true,
        itemCount: listQuestion[index].choix.length,
        itemBuilder: (context, indexRadio) {
//          if (_visibleRadioButton) {
          return new InkWell(
            onTap: () {
              radioEvent(indexRadio);
            },
            child: new Container(
                decoration: new BoxDecoration(
                  color: radioGroup == indexRadio
                      ? Colors.lightBlueAccent
                      : Colors.black12,
                  border: new Border.all(
                      width: 1.5,
                      color: radioGroup == indexRadio
                          ? Colors.blueAccent
                          : Colors.grey),
                ),
                margin: EdgeInsets.all(16),
                padding: EdgeInsets.fromLTRB(10, 10, 10, 0),
                child: new Container(
                  padding: EdgeInsets.only(left: 10),
                  child: new Column(
                    children: <Widget>[
                      new Text('${listQuestion[index].choix[indexRadio]}',
                          style:
                              new TextStyle(color: Colors.brown, fontSize: 14)),
                      new Container(
                        child: new Radio(
                            visualDensity:
                                VisualDensity.adaptivePlatformDensity,
                            value: indexRadio,
                            activeColor: Colors.lightBlueAccent,
                            groupValue: radioGroup,
                            onChanged: radioEvent),
                      ),
                    ],
                  ),
                )),
          );
          //        }
        });

    var cardCheckBox = ListView.builder(
        scrollDirection: Axis.vertical,
        physics: NeverScrollableScrollPhysics(),
        shrinkWrap: true,
        itemCount: listQuestion[index].choix.length,
        itemBuilder: (BuildContext ctxt, int indexCheckBox) {
//          if (_visibleCheckBox) {
          return new GestureDetector(
            onTap: () {
              print("onTap $indexCheckBox");
              setState(() {
                checkboxGroup[indexCheckBox] = !checkboxGroup[indexCheckBox];
                checkBoxEvent(indexCheckBox, checkboxGroup[indexCheckBox]);
              });
            },
            child: new Container(
                decoration: new BoxDecoration(
                  color: checkboxGroup[indexCheckBox]
                      ? Colors.lightBlueAccent
                      : Colors.transparent,
                  border: new Border.all(
                      width: 1.0,
                      color: checkboxGroup[indexCheckBox]
                          ? Colors.lightBlueAccent
                          : Colors.grey),
                ),
                margin: EdgeInsets.all(22),
                padding: EdgeInsets.fromLTRB(10, 20, 10, 20),
                child: new Container(
                  padding: EdgeInsets.only(left: 10),
                  child: new Column(
                    children: <Widget>[
                      new Text('${listQuestion[index].choix[indexCheckBox]}',
                          style:
                              new TextStyle(color: Colors.grey, fontSize: 18)),
                      new Container(
                        child: new Checkbox(
                          onChanged: (value) => {
                            //   print(value),
                            checkBoxEvent(indexCheckBox, value)
                          },
                          value: checkboxGroup[indexCheckBox],
                          checkColor: Colors.lightBlueAccent,
                          activeColor: Colors.lightBlueAccent,
                        ),
                      ),
                    ],
                  ),
                )),
          );
          //        }
        });

    var cardSpinner = new DropdownButton<String>(
      isExpanded: true,
      value: dropdownValueSpinnerChoice,
      icon: Icon(Icons.arrow_drop_down),
      iconSize: 50,
      elevation: 16,
      style: TextStyle(color: Colors.lightBlueAccent, fontSize: 20),
      underline: new Container(
        height: 2,
        color: Colors.lightBlueAccent,
      ),
      onChanged: (String data) {
        setState(() {
          dropdownValueSpinnerChoice = data;
          _textFAnswersController.text = data;
        });
      },
      items: listChoiceSpinner.map<DropdownMenuItem<String>>((String value) {
        return DropdownMenuItem<String>(
          value: value,
          child: new Center(child: Text(value)),
        );
      }).toList(),
    );

    var cardLayou1 = new Card(
      elevation: 20,
      child: new SizedBox(
        width: double.maxFinite,
        child: new Center(
          child: new Container(
            child: getDropDownLanguage(),
          ),
        ),
      ),
    );
    TextStyle style = TextStyle(
        fontFamily: 'Montserrat', fontSize: 20.0, color: Colors.black);

    var cardLayout2 = new Card(
      elevation: 20,
      child: new Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          questionField,
          new SizedBox(height: 40),
          new Container(
              child: new Visibility(
              visible: _visibileTextField,
              child:new TextField(
            controller: _textFAnswersController,
            enabled: _enableAnswers,
            keyboardType: TextInputType.multiline,
            maxLines: null,
            style: style,
          ))),
          new SizedBox(height: 40),
          new Visibility(
            child: cardRadioButton,
            visible: _visibleRadioButton,
          ),
          new Visibility(child: cardCheckBox, visible: _visibleCheckBox),
          new Visibility(
              child: new Column(children: <Widget>[cardSpinner]),
              visible: _visibleSpinner),
        ],
      ),
    );

    return new Scaffold(
      key: scaffoldState,
      appBar: new AppBar(
        backgroundColor: Colors.lightBlue,
        title: new Text(_store.get("name")),
        actions: <Widget>[
          new Row(
            children: <Widget>[
              new Visibility(
                maintainSize: true,
                maintainAnimation: true,
                maintainState: true,
                visible: _visibleProgress,
                child: new CircularProgressIndicator(
                  backgroundColor: Colors.white70,
                ),
              )
            ],
          )
        ],
      ),
      bottomNavigationBar: new BottomNavigationBar(
        items: [
          new BottomNavigationBarItem(
              icon: new Icon(Icons.chevron_left),
              title: new Text("Previous"),
              backgroundColor: Colors.lightBlue),
          new BottomNavigationBarItem(
              icon: new Icon(Icons.navigate_next_rounded),
              title: new Text("Next"),
              backgroundColor: Colors.lightBlue),
        ],
        onTap: (int x) => {onClickNavigation(x)},
      ),
      body: // new Column(
          //  children: [
          new SingleChildScrollView(
              physics: ScrollPhysics(),
              padding: EdgeInsets.all(16),
              child: new Visibility(
                  child: new Column(
                children: [
                  cardLayou1,
                  cardLayout2,
//                   test,
                ],
              ))),
      //],
      //),
    );
  }

  readMessage() {
    Map<String, dynamic> Alldata;
    DatabaseReference _firebaseRef = FirebaseDatabase().reference();
    _firebaseRef
        .child("FORMS")
        .child(_keyForm.toString())
        .onValue
        .listen((event) async {
      listQuestion = [];
      listReponse = [];
      index = 0;
      var snapshot = event.snapshot;
      Alldata = Map.from(snapshot.value);

      _store.set("ALLData", Alldata);
      listQuestion = await getQuestionFromList(Alldata);

      print("length = ${listQuestion.length}  ${listQuestion[1]}");
      await readAnswers();
      setQuestion(index);
      setState(() {
        _visibleProgress = false;
        testWithType();
      });
    });
  }

  List getQuestionFromList(data) {
    List<DataQuestions> tempQuest = [];
    if (data != null) {
      data["QUESTIONS"].forEach((index, data) {
        data.forEach((index, element) {
          Map<dynamic, dynamic> choix;
          if (element["choix"] != null) {
            choix = element["choix"].asMap();
          } else {
            choix = new Map();
          }
          tempQuest.add(new DataQuestions(
              element["key_value"],
              element["key_form"],
              Map.from(element["questions"]),
              element["question"],
              element["num_Item"],
              element["group"],
              element["typeQuestion"],
              element["shared"],
              (choix)));

          listReponse.add(new DataAnswers(_keyForm, null, element["key_value"],
              userId, element["question"], "", element["num_Item"], index));
        });
      });
      // sort questions and answers by order (num_Item)
      tempQuest.sort((a, b) => a.num_Item.compareTo(b.num_Item));
      listReponse.sort((a, b) => a.num_Item.compareTo(b.num_Item));
    }
    return tempQuest;
  }

  sendMessage() {
    DatabaseReference _firebaseRef = FirebaseDatabase()
        .reference()
        .child('FORMS_Data')
        .child(_keyForm)
        .child(userId);

    _firebaseRef
        .child(listQuestion[index].group)
        .child(listQuestion[index].key_value)
        .set(listReponse[index].toJson());
  }

  readAnswers() {
    Map<String, dynamic> AlldataAnswers;
    DatabaseReference _firebaseRef = FirebaseDatabase().reference();
    _firebaseRef
        .child("FORMS_Data")
        .child(_keyForm.toString())
        .child(userId)
        .onValue
        .listen((event) {
      var snapshot = event.snapshot;
      AlldataAnswers = Map.from(snapshot.value);
      getAnswersFromList(AlldataAnswers);
    });
  }

  getAnswersFromList(data) {
    setState(() {
      index = -1;
      data.forEach((numb, data) {
        data.forEach((numb, element) {
          index++;
          print("getAnswers index=$index");
          var c = (element["num_Item"] - 1);
          listReponse[c].key_form = element["key_form"];
          listReponse[c].key_value = element["key_value"];
          listReponse[c].key_value_Quest = element["key_value_Quest"];
          listReponse[c].user = element["user"];
          listReponse[c].question = element["question"];
          listReponse[c].answer = element["answer"];
          listReponse[c].num_Item = element["num_Item"];
          listReponse[c].group = element["group"];
        });
        //       onClickNavigation(1);
      });
    });

    if(index== listQuestion.length - 1){
      finishFrom();
    return ;
    }
    if (index == -1 || index < listQuestion.length - 1) {
      index++;
    }
    testWithType();
    setQuestion(index);
  }

  void SnackDialog(message) {
    scaffoldState.currentState
        .showSnackBar(new SnackBar(content: new Text(message)));
  }

  testWithType() {
    _visibleCheckBox = false;
    _visibleRadioButton = false;
    _visibleSpinner = false;
    _enableAnswers = true;
    if (index > listQuestion.length) return;

    switch (listQuestion[index].typeQuestion) {
      case "RadioButton":
        setState(() {
          listQuestion[index].choix.forEach((key, value) {
            if (value == _textFAnswersController.text) {
              radioGroup = key;
            }
          });

          _visibleRadioButton = true;
          _visibleCheckBox = false;
          _visibleSpinner = false;
          _enableAnswers = false;
        });
        break;
      case "CheckBox":
        setState(() {
          print("hello checkBox");
          _visibleCheckBox = true;
          _enableAnswers = false;
          _visibleSpinner = false;
          _visibleRadioButton = false;
          checkboxGroup.clear();
          for (int i = 0; i < listQuestion[index].choix.length; i++) {
            if (_textFAnswersController.text
                .contains("${listQuestion[index].choix[i]} ; ")) {
              checkboxGroup.add(true);
            } else {
              checkboxGroup.add(false);
            }
          }
        });
        break;
      case "Spinner":
        setState(() {
          listChoiceSpinner = [];
          dropdownValueSpinnerChoice = null;
          _visibleSpinner = true;
          _visibleCheckBox = false;
          _enableAnswers = false;
          _visibleRadioButton = false;
          listQuestion[index].choix.forEach((key, value) {
            listChoiceSpinner.add(value);
          });
          listQuestion[index].choix.forEach((key, value) {
            if (value == _textFAnswersController.text) {
              dropdownValueSpinnerChoice = value;
            }
          });
        });
        break;
      default:
        // "TextField":
        setState(() {
          _enableAnswers = true;
          _visibleCheckBox = false;
          _visibleRadioButton = false;
        });
    }
  }

  finishFrom() {
    print("finish function ");
      _finish=true;
    setState(() {
      _enableAnswers = false;
      _visibileTextField=false;
      _visibleCheckBox = false;
      _visibleRadioButton = false;
      _visibleSpinner = false;
      if (dropdownValueLanguage == "AR") {
        Question = Languages.finishquestion[0];
      } else {
        Question = Languages.finishquestion[1];
      }
    });
  }
}
