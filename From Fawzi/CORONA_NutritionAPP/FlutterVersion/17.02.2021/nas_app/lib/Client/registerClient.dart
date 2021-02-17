import 'package:custom_radio_grouped_button/custom_radio_grouped_button.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:firebase_database/firebase_database.dart';

import 'package:flutter_holo_date_picker/flutter_holo_date_picker.dart';
import 'package:geolocator/geolocator.dart';
import 'package:geocoder/geocoder.dart';
import 'package:nas_app/UserData.dart';
import 'package:nas_app/main.dart';
import 'package:nas_app/resources/Languages.dart';


import 'mainClient.dart';

class register extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return new StateRegister();
  }
}

class StateRegister extends State<register> {
  final databaseReference = FirebaseDatabase.instance.reference();

  UserData userData;

  final GlobalKey<ScaffoldState> scaffoldState = new GlobalKey<ScaffoldState>();
  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();

  bool _visibleProgress = false;
  bool _obscureText1 = true, _obscureText2 = true;

  List<String> listLanguage = ['AR', 'EN'];
  String dropdownValueLanguage = "AR";
  int language = 0;

  DateTime _selectedDate;
  final TextEditingController _controllerWeight = TextEditingController();
  final TextEditingController _controllerHeight = TextEditingController();
  final TextEditingController _controllerNbFamily = TextEditingController();
  final TextEditingController _controllerNbChildrenResp =
      TextEditingController();
  final TextEditingController _controllerNbRooms = TextEditingController();
  final TextEditingController _controllerNbCiguarette = TextEditingController();
  final TextEditingController _controllerNbChildren = TextEditingController();
  final TextEditingController _controllerNbNarguile = TextEditingController();

  final TextEditingController _controllerEmail = TextEditingController();
  final TextEditingController _controllerUsername = TextEditingController();
  final TextEditingController _controllerPassword = TextEditingController();
  final TextEditingController _controllerConfirmPassword =
      TextEditingController();

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    userData = new UserData();
    getCountryName();

    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    var dropDownLanguage = new DropdownButton<String>(
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
          if (data == "AR")
            language = 0;
          else
            language = 1;
          //_fieldDesc = _descForm[dropdownValueLanguage];
        });
      },
      items: listLanguage.map<DropdownMenuItem<String>>((String value) {
        return DropdownMenuItem<String>(
          value: value,
          child: new Center(child: Text(value)),
        );
      }).toList(),
    );

    var intro = new Card(
        borderOnForeground: true,
        color: Colors.white70,
        child: new Container(
          padding: EdgeInsets.fromLTRB(16, 16, 16, 16),
          child: new Text(Languages.introduction[language],
              textDirection:
                  language == 0 ? TextDirection.rtl : TextDirection.ltr),
        ));

    var dateBirth = new Card(
        color: Colors.lightBlueAccent,
        borderOnForeground: true,
        shape: OutlineInputBorder(borderRadius: BorderRadius.circular(32.0)),
        child: new Container(
          padding: EdgeInsets.fromLTRB(16, 16, 16, 16),
          child: DatePickerWidget(
            looping: false,
            // default is not looping
            firstDate: DateTime(1940, 01, 01),
            lastDate: DateTime(2020, 1, 1),
            initialDate: DateTime(2020, 1, 1),
            dateFormat: "dd-MMM-yyyy",
            locale: DatePicker.localeFromString('en'),
            onChange: (DateTime newDate, _) => {
              _selectedDate = newDate,
              userData.date_naissance = _selectedDate.toString()
            },
            pickerTheme: DateTimePickerTheme(
              itemTextStyle: TextStyle(color: Colors.black, fontSize: 19),
              dividerColor: Colors.lightBlueAccent,
            ),
          ),
        ));

    var genre = new Card(
        color: Colors.lightBlueAccent,
        borderOnForeground: true,
        shape: OutlineInputBorder(borderRadius: BorderRadius.circular(32.0)),
        child: new Container(
            padding: EdgeInsets.fromLTRB(16, 16, 16, 16),
            child: new Column(children: <Widget>[
              new Text("Gender"),
              CustomRadioButton(
                height: 50,
                horizontal: true,
                padding: 5,
                autoWidth: true,
                enableShape: true,
                buttonLables: [
                  Languages.male[language],
                  Languages.female[language],
                ],
                buttonValues: [
                  Languages.male[language],
                  Languages.female[language],
                ],
                radioButtonValue: (value) => {
                  print(value),
                  userData.sexe = value,
                },
                selectedColor: Theme.of(context).accentColor,
                unSelectedColor: Theme.of(context).canvasColor,
              )
            ])));

    TextStyle styleField = TextStyle(fontFamily: 'Montserrat', fontSize: 18);

    var fieldWeight = TextFormField(
      keyboardType: TextInputType.number,
      controller: _controllerWeight,
      style: styleField,
      decoration: InputDecoration(
          contentPadding: EdgeInsets.fromLTRB(16, 16, 16, 16),
          hintText: Languages.poids_actuel[language],
          border:
              OutlineInputBorder(borderRadius: BorderRadius.circular(20.0))),
      validator: (String value) {
        if (value.isEmpty) {
          return 'Please enter some text';
        }
        return null;
      },
    );

    var fieldHeight = TextFormField(
      keyboardType: TextInputType.number,
      controller: _controllerHeight,
      style: styleField,
      decoration: InputDecoration(
          contentPadding: EdgeInsets.fromLTRB(16, 16, 16, 16),
          hintText: Languages.taille_actuelle[language],
          border:
              OutlineInputBorder(borderRadius: BorderRadius.circular(20.0))),
      validator: (String value) {
        if (value.isEmpty) {
          return 'Please enter some text';
        }
        return null;
      },
    );

    var radiolevelEducation = new Card(
        color: Colors.lightBlueAccent,
        borderOnForeground: true,
        shape: OutlineInputBorder(borderRadius: BorderRadius.circular(32.0)),
        child: new Container(
            padding: EdgeInsets.fromLTRB(16, 16, 16, 16),
            child: new Column(
              children: <Widget>[
                new Text(Languages.niveau_edu[language]),
                CustomRadioButton(
                  height: 50,
                  horizontal: true,
                  padding: 5,
                  autoWidth: true,
                  enableShape: true,
                  buttonLables: [
                    Languages.niveau_edu0[language],
                    Languages.niveau_edu1[language],
                    Languages.niveau_edu2[language],
                    Languages.niveau_edu3[language],
                    Languages.niveau_edu4[language],
                  ],
                  buttonValues: [
                    Languages.niveau_edu0[language],
                    Languages.niveau_edu1[language],
                    Languages.niveau_edu2[language],
                    Languages.niveau_edu3[language],
                    Languages.niveau_edu4[language],
                  ],
                  radioButtonValue: (value) => {
                    userData.niveau_Education = value,
                    print(value),
                  },
                  selectedColor: Theme.of(context).accentColor,
                  unSelectedColor: Theme.of(context).canvasColor,
                ),
                //  new Text("ds"),
              ],
            )));

    var radiolive = new Card(
        color: Colors.lightBlueAccent,
        borderOnForeground: true,
        shape: OutlineInputBorder(borderRadius: BorderRadius.circular(32.0)),
        child: new Container(
            padding: EdgeInsets.fromLTRB(16, 16, 16, 16),
            child: new Column(
              children: <Widget>[
                new Text(Languages.lieu_residance[language]),
                CustomRadioButton(
                  height: 50,
                  horizontal: true,
                  padding: 5,
                  autoWidth: true,
                  enableShape: true,
                  buttonLables: [
                    Languages.lieu_1[language],
                    Languages.lieu_2[language],
                    Languages.lieu_3[language],
                    Languages.lieu_4[language],
                    Languages.lieu_5[language],
                    Languages.lieu_6[language],
                    Languages.lieu_7[language],
                    Languages.lieu_8[language],
                  ],
                  buttonValues: [
                    Languages.lieu_1[language],
                    Languages.lieu_2[language],
                    Languages.lieu_3[language],
                    Languages.lieu_4[language],
                    Languages.lieu_5[language],
                    Languages.lieu_6[language],
                    Languages.lieu_7[language],
                    Languages.lieu_8[language],
                  ],
                  radioButtonValue: (value) =>
                      {userData.lieu_Resistance = value, print(value)},
                  selectedColor: Theme.of(context).accentColor,
                  unSelectedColor: Theme.of(context).canvasColor,
                )
              ],
            )));

    var radioSocialStatus = new Card(
        color: Colors.lightBlueAccent,
        borderOnForeground: true,
        shape: OutlineInputBorder(borderRadius: BorderRadius.circular(32.0)),
        child: new Container(
            padding: EdgeInsets.fromLTRB(16, 16, 16, 16),
            child: new Column(
              children: <Widget>[
                new Text(Languages.situation_sociale[language]),
                CustomRadioButton(
                  height: 50,
                  horizontal: true,
                  padding: 5,
                  autoWidth: true,
                  enableShape: true,
                  buttonLables: [
                    Languages.situation1[language],
                    Languages.situation2[language],
                    Languages.situation3[language],
                    Languages.situation4[language],
                  ],
                  buttonValues: [
                    Languages.situation1[language],
                    Languages.situation2[language],
                    Languages.situation3[language],
                    Languages.situation4[language],
                  ],
                  radioButtonValue: (value) => {
                    userData.situation_Sociale = value,
                    print(value),
                  },
                  selectedColor: Theme.of(context).accentColor,
                  unSelectedColor: Theme.of(context).canvasColor,
                )
              ],
            )));

    var fieldNbFamily = TextFormField(
      keyboardType: TextInputType.number,
      controller: _controllerNbFamily,
      style: styleField,
      decoration: InputDecoration(
          contentPadding: EdgeInsets.fromLTRB(16, 16, 16, 16),
          hintText: Languages.nb_prs_famille[language],
          border:
              OutlineInputBorder(borderRadius: BorderRadius.circular(20.0))),
      validator: (String value) {
        if (value.isEmpty) {
          return 'Please enter some text';
        }
        return null;
      },
    );

    var fieldNbChildren = TextFormField(
      keyboardType: TextInputType.number,
      controller: _controllerNbChildren,
      style: styleField,
      decoration: InputDecoration(
          contentPadding: EdgeInsets.fromLTRB(16, 16, 16, 16),
          hintText: Languages.nb_fils[language],
          border:
              OutlineInputBorder(borderRadius: BorderRadius.circular(20.0))),
      validator: (String value) {
        if (value.isEmpty) {
          return 'Please enter some text';
        }
        return null;
      },
    );

    var fieldNbChildrenResponsible = TextFormField(
      keyboardType: TextInputType.number,
      controller: _controllerNbChildrenResp,
      style: styleField,
      decoration: InputDecoration(
          contentPadding: EdgeInsets.fromLTRB(16, 16, 16, 16),
          hintText: Languages.nb_fils_prs[language],
          border:
              OutlineInputBorder(borderRadius: BorderRadius.circular(20.0))),
      validator: (String value) {
        if (value.isEmpty) {
          return 'Please enter some text';
        }
        return null;
      },
    );

    var fieldNbRooms = TextFormField(
      keyboardType: TextInputType.number,
      controller: _controllerNbRooms,
      style: styleField,
      decoration: InputDecoration(
          contentPadding: EdgeInsets.fromLTRB(16, 16, 16, 16),
          hintText: Languages.nb_chambres[language],
          border:
              OutlineInputBorder(borderRadius: BorderRadius.circular(20.0))),
      validator: (String value) {
        if (value.isEmpty) {
          return 'Please enter some text';
        }
        return null;
      },
    );

    var radioOccupationalStatus = new Card(
        color: Colors.lightBlueAccent,
        borderOnForeground: true,
        shape: OutlineInputBorder(borderRadius: BorderRadius.circular(32.0)),
        child: new Container(
            padding: EdgeInsets.fromLTRB(16, 16, 16, 16),
            child: new Column(
              children: <Widget>[
                new Text(Languages.cas_professionnel[language]),
                CustomRadioButton(
                  height: 50,
                  horizontal: true,
                  padding: 5,
                  autoWidth: true,
                  enableShape: true,
                  buttonLables: [
                    Languages.cas_1[language],
                    Languages.cas_2[language],
                    Languages.cas_3[language],
                    Languages.cas_4[language],
                  ],
                  buttonValues: [
                    Languages.cas_1[language],
                    Languages.cas_2[language],
                    Languages.cas_3[language],
                    Languages.cas_4[language],
                  ],
                  radioButtonValue: (value) => {
                    userData.cas_professionnel = value,
                    print(value),
                  },
                  selectedColor: Theme.of(context).accentColor,
                  unSelectedColor: Theme.of(context).canvasColor,
                )
              ],
            )));

    var radioHealthcare = new Card(
        color: Colors.lightBlueAccent,
        borderOnForeground: true,
        shape: OutlineInputBorder(borderRadius: BorderRadius.circular(32.0)),
        child: new Container(
            padding: EdgeInsets.fromLTRB(16, 16, 16, 16),
            child: new Column(
              children: <Widget>[
                new Text(Languages.spec_sante[language]),
                new Center(
                    child: CustomRadioButton(
                  height: 50,
                  horizontal: true,
                  padding: 5,
                  enableShape: true,
                  buttonLables: [
                    Languages.text_yes[language],
                    Languages.text_no[language],
                  ],
                  buttonValues: [
                    Languages.text_yes[language],
                    Languages.text_no[language],
                  ],
                  radioButtonValue: (value) => {
                    userData.specialiste_sante = value,
                    print(value),
                  },
                  selectedColor: Theme.of(context).accentColor,
                  unSelectedColor: Theme.of(context).canvasColor,
                ))
              ],
            )));

    var radioAssurance = new Card(
        color: Colors.lightBlueAccent,
        borderOnForeground: true,
        shape: OutlineInputBorder(borderRadius: BorderRadius.circular(32.0)),
        child: new Container(
            padding: EdgeInsets.fromLTRB(16, 16, 16, 16),
            child: new Column(
              children: <Widget>[
                new Text(Languages.assurance[language]),
                CustomRadioButton(
                  height: 50,
                  horizontal: true,
                  padding: 5,
                  autoWidth: true,
                  enableShape: true,
                  buttonLables: [
                    Languages.ass_1[language],
                    Languages.ass_2[language],
                    Languages.ass_3[language],
                    Languages.ass_4[language],
                  ],
                  buttonValues: [
                    Languages.ass_1[language],
                    Languages.ass_2[language],
                    Languages.ass_3[language],
                    Languages.ass_4[language],
                  ],
                  radioButtonValue: (value) => {
                    userData.assurance = value,
                    print(value),
                  },
                  selectedColor: Theme.of(context).accentColor,
                  unSelectedColor: Theme.of(context).canvasColor,
                )
              ],
            )));

    var radioAlcool = new Card(
        color: Colors.lightBlueAccent,
        borderOnForeground: true,
        shape: OutlineInputBorder(borderRadius: BorderRadius.circular(32.0)),
        child: new Container(
            padding: EdgeInsets.fromLTRB(16, 16, 16, 16),
            child: new Column(
              children: <Widget>[
                new Text(Languages.quest16[language]),
                CustomRadioButton(
                  height: 50,
                  horizontal: true,
                  padding: 5,
                  autoWidth: true,
                  enableShape: true,
                  buttonLables: [
                    Languages.quest16_1[language],
                    Languages.quest16_2[language],
                    Languages.quest16_3[language],
                  ],
                  buttonValues: [
                    Languages.quest16_1[language],
                    Languages.quest16_2[language],
                    Languages.quest16_3[language],
                  ],
                  radioButtonValue: (value) => {
                    userData.alcool = value,
                    print(value),
                  },
                  selectedColor: Theme.of(context).accentColor,
                  unSelectedColor: Theme.of(context).canvasColor,
                )
              ],
            )));

    var fieldQuantifyCigarettes = TextFormField(
      keyboardType: TextInputType.number,
      controller: _controllerNbCiguarette,
      style: styleField,
      decoration: InputDecoration(
          filled: true,
          fillColor: Colors.white,
          contentPadding: EdgeInsets.fromLTRB(32, 16, 32, 16),
          hintText: Languages.quantite[language],
          border:
              OutlineInputBorder(borderRadius: BorderRadius.circular(10.0))),
      validator: (String value) {
        if (value.isEmpty) {
          return 'Please enter some text';
        }
        return null;
      },
    );

    var radioCigarettes = new Card(
        color: Colors.lightBlueAccent,
        borderOnForeground: true,
        shape: OutlineInputBorder(borderRadius: BorderRadius.circular(32.0)),
        child: new Container(
            padding: EdgeInsets.fromLTRB(16, 16, 16, 16),
            child: new Column(
              children: <Widget>[
                new Text(Languages.quest17[language]),
                CustomRadioButton(
                  height: 50,
                  horizontal: true,
                  padding: 5,
                  autoWidth: true,
                  enableShape: true,
                  buttonLables: [
                    Languages.quest1718_1[language],
                    Languages.quest1718_2[language],
                    Languages.quest1718_3[language],
                    Languages.quest1718_4[language],
                  ],
                  buttonValues: [
                    Languages.quest1718_1[language],
                    Languages.quest1718_2[language],
                    Languages.quest1718_3[language],
                    Languages.quest1718_4[language],
                  ],
                  radioButtonValue: (value) => {
                    userData.bolciguarette = value,
                    if (value == Languages.quest1718_4[language])
                      {
                        _controllerNbCiguarette.text = 0.toString(),
                        print(value),
                      },
                  },
                  selectedColor: Theme.of(context).accentColor,
                  unSelectedColor: Theme.of(context).canvasColor,
                ),
                fieldQuantifyCigarettes,
              ],
            )));

    var fieldQuantifyNarguile = TextFormField(
      keyboardType: TextInputType.number,
      controller: _controllerNbNarguile,
      style: styleField,
      decoration: InputDecoration(
          filled: true,
          fillColor: Colors.white,
          contentPadding: EdgeInsets.fromLTRB(16, 16, 16, 16),
          hintText: Languages.quantite[language],
          border:
              OutlineInputBorder(borderRadius: BorderRadius.circular(10.0))),
      validator: (String value) {
        if (value.isEmpty) {
          return 'Please enter some text';
        }
        return null;
      },
    );

    var radioNarguile = new Card(
        color: Colors.lightBlueAccent,
        borderOnForeground: true,
        shape: OutlineInputBorder(borderRadius: BorderRadius.circular(32.0)),
        child: new Container(
            padding: EdgeInsets.fromLTRB(16, 16, 16, 16),
            child: new Column(
              children: <Widget>[
                new Text(Languages.quest18[language]),
                CustomRadioButton(
                  height: 50,
                  horizontal: true,
                  padding: 5,
                  autoWidth: true,
                  enableShape: true,
                  buttonLables: [
                    Languages.quest1718_1[language],
                    Languages.quest1718_2[language],
                    Languages.quest1718_3[language],
                    Languages.quest1718_4[language],
                  ],
                  buttonValues: [
                    Languages.quest1718_1[language],
                    Languages.quest1718_2[language],
                    Languages.quest1718_3[language],
                    Languages.quest1718_4[language],
                  ],
                  radioButtonValue: (value) => {
                    if (value == Languages.quest1718_4[language])
                      {
                        _controllerNbNarguile.text = 0.toString(),
                      },
                    userData.bolnarguile = value,
                    print(value),
                  },
                  selectedColor: Theme.of(context).accentColor,
                  unSelectedColor: Theme.of(context).canvasColor,
                ),
                fieldQuantifyNarguile,
              ],
            )));

    var fieldEmail = TextFormField(
      keyboardType: TextInputType.emailAddress,
      controller: _controllerEmail,
      style: styleField,
      decoration: InputDecoration(
          contentPadding: EdgeInsets.fromLTRB(16, 16, 16, 16),
          hintText: Languages.email[language],
          border:
              OutlineInputBorder(borderRadius: BorderRadius.circular(20.0))),
      validator: (String value) {
        if (!value.isEmpty) {
          var email = value;
          bool emailValid = RegExp(
                  r"^[a-zA-Z0-9.a-zA-Z0-9.!#$%&'*+-/=?^_`{|}~]+@[a-zA-Z0-9]+\.[a-zA-Z]+")
              .hasMatch(email);
          if (!emailValid) {
            return 'Invalid email';
          }
        }
        return null;
      },
    );

    var fieldUserName = TextFormField(
      controller: _controllerUsername,
      style: styleField,
      decoration: InputDecoration(
          contentPadding: EdgeInsets.fromLTRB(16, 16, 16, 16),
          hintText: Languages.username[language],
          border:
              OutlineInputBorder(borderRadius: BorderRadius.circular(20.0))),
      validator: (String value) {
        if (value.isEmpty) {
          return 'Please enter some text';
        }
        return null;
      },
    );

    var fieldPassword = TextFormField(
      onTap: () {
        setState(() {
          _obscureText1 = !_obscureText1;
        });
      },
      obscureText: _obscureText1,
      controller: _controllerPassword,
      style: styleField,
      decoration: InputDecoration(
          contentPadding: EdgeInsets.fromLTRB(16, 16, 16, 16),
          hintText: Languages.password[language],
          border:
              OutlineInputBorder(borderRadius: BorderRadius.circular(20.0))),
      validator: (String value) {
        if (value.trim().isEmpty) {
          return 'Please enter some text';
        }
        return null;
      },
    );

    var fieldConfirmPassword = TextFormField(
      onTap: () {
        setState(() {
          _obscureText2 = !_obscureText2;
        });
      },
      obscureText: _obscureText2,
      controller: _controllerConfirmPassword,
      style: styleField,
      decoration: InputDecoration(
          contentPadding: EdgeInsets.fromLTRB(16, 16, 16, 16),
          hintText: Languages.confirmPassword[language],
          border:
              OutlineInputBorder(borderRadius: BorderRadius.circular(20.0))),
      validator: (String value) {
        if (value.trim().isEmpty) {
          return 'Please enter some text';
        }
        if (value != _controllerPassword.text) {
          return 'Password error';
        }

        return null;
      },
    );

    return new Scaffold(
      key: scaffoldState,
      appBar: new AppBar(
        backgroundColor: Colors.lightBlue,
        title: new Text("Register"),
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
//      bottomNavigationBar: new BottomNavigationBar(
//        items: [
//          new BottomNavigationBarItem(
//              icon: new Icon(Icons.chevron_left),
//              title: new Text("Previous"),
//              backgroundColor: Colors.lightBlue),
//          new BottomNavigationBarItem(
//              icon: new Icon(Icons.navigate_next_rounded),
//              title: new Text("Next"),
//              backgroundColor: Colors.lightBlue),
//        ],
//        //   onTap: (int x) => {onClickNavigation(x)},
//      ),

      body: new SingleChildScrollView(
          physics: ScrollPhysics(),
          padding: EdgeInsets.all(16),
          child: new Center(
              child: new Form(
            key: _formKey,
            child: new Padding(
                padding: EdgeInsets.all(0),
                child: new Column(
                  children: <Widget>[
                    dropDownLanguage,
                    SizedBox(height: 15.0),
                    intro,
                    SizedBox(height: 15.0),
                    dateBirth,
                    SizedBox(height: 15.0),
                    genre,
                    SizedBox(height: 15.0),
                    fieldWeight,
                    SizedBox(height: 15.0),
                    fieldHeight,
                    SizedBox(height: 15.0),
                    radiolevelEducation,
                    SizedBox(height: 15.0),
                    radiolive,
                    SizedBox(height: 15.0),
                    radioSocialStatus,
                    SizedBox(height: 15.0),
                    fieldNbFamily,
                    SizedBox(height: 15.0),
                    fieldNbChildren,
                    SizedBox(height: 15.0),
                    fieldNbChildrenResponsible,
                    SizedBox(height: 15.0),
                    fieldNbRooms,
                    SizedBox(height: 15.0),
                    radioOccupationalStatus,
                    SizedBox(height: 15.0),
                    radioHealthcare,
                    SizedBox(height: 15.0),
                    radioAssurance,
                    SizedBox(height: 15.0),
                    radioAlcool,
                    SizedBox(height: 15.0),
                    radioCigarettes,
                    SizedBox(height: 15.0),
                    radioNarguile,
                    SizedBox(height: 15.0),
                    fieldEmail,
                    SizedBox(height: 15.0),
                    fieldUserName,
                    SizedBox(height: 15.0),
                    fieldPassword,
                    SizedBox(height: 15.0),
                    fieldConfirmPassword,
                    SizedBox(height: 15.0),

                    Container(
//                      padding: const EdgeInsets.symmetric(vertical: 16.0),
                      alignment: Alignment.center,
                      child: Material(
                        elevation: 10.0,
                        borderRadius: BorderRadius.circular(20.0),
                        color: Colors.lightBlue,
                        child: MaterialButton(
                          child: Text(
                            "Register",
                            style: TextStyle(
                                fontFamily: 'Montserrat', fontSize: 18.0,
                                color:Colors.white, fontWeight: FontWeight.bold),
                          ),
                          onPressed: () async {
                            if (_formKey.currentState.validate() &&
                                userData.sexe != "" &&
                                userData.niveau_Education != "" &&
                                userData.lieu_Resistance != "" &&
                                userData.situation_Sociale != "" &&
                                userData.cas_professionnel != "" &&
                                userData.specialiste_sante != "" &&
                                userData.assurance != "" &&
                                userData.alcool != "" &&
                                userData.bolciguarette != "" &&
                                userData.bolnarguile != "") {
                              userData.poids = _controllerWeight.text;
                              userData.taille = _controllerHeight.text;
                              userData.nb_prs_famille =
                                  _controllerNbFamily.text;
                              userData.nb_fils = _controllerNbChildren.text;
                              userData.nb_fils_prs =
                                  _controllerNbChildrenResp.text;
                              userData.nb_chambres = _controllerNbRooms.text;
                              userData.nb_ciguarette =
                                  _controllerNbCiguarette.text;
                              userData.nb_Narguile = _controllerNbNarguile.text;
                              userData.username = _controllerUsername.text;
                              userData.email = _controllerEmail.text;

                              DateTime now = DateTime.now();
                              var currentTime = new DateTime(now.year,
                                  now.month, now.day, now.hour, now.minute);
                              userData.date_Register = currentTime.toString();
                              registerclient();
                            } else {
                              SnackDialog("Error entry");
                            }
                          },
                        ),
                      ),
                    )
                  ],
                )),
          ))),
      //],
      //),
    );
  }

  registerclient() async {
    showProgress();

    try {
      ///////
      final FirebaseAuth _auth = FirebaseAuth.instance;
      final User user = (await _auth.createUserWithEmailAndPassword(
        email: _controllerUsername.text + "@CovidNutrUl.com",
        password: _controllerPassword.text,
      ))
          .user;

      if (user != null) {
        if (!user.emailVerified) {
          await user.sendEmailVerification();
        }
        await user.updateProfile(displayName: "fmm1");
        SnackDialog(" register");
        await showDialog(
            context: context,
            builder: (BuildContext context) {
              return AlertDialog(
                title: new Text("Confidentiality and Data Security"),
                content: Text(Languages.note_Register[language]),
                shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.all(Radius.circular(2.0))),
                actions: <Widget>[
                  new FlatButton(
                      onPressed: () {
                        Navigator.of(context).pushReplacement(MaterialPageRoute(
                            builder: (context) =>
                                MyApp(
                                  //      user: user1,
                                )));
                      },
                      child: new Text("Ok "))
                ],
              );
            });
        Navigator.of(context).pushReplacement(MaterialPageRoute(
            builder: (context) =>
                MyApp(
                  //      user: user1,
                )));
      

    } else {
//      _isSuccess = false;
        SnackDialog("Register failed ");
        return;
      }

//////////

      DatabaseReference _firebaseRef =
      FirebaseDatabase().reference().child('USERS');
      userData.user_id = user.uid;
      print(userData.user_id);
      _firebaseRef
          .child(user.uid)
          .set(userData.toJson())
          .whenComplete(() => {SnackDialog("Done"), hideProgress()});
    hideProgress();
    }catch(e){
      SnackDialog(e.toString());
      hideProgress();
      return ;
    }
    }

  void SnackDialog(message) {
    scaffoldState.currentState
        .showSnackBar(new SnackBar(content: new Text(message)));
  }

  void showProgress() {
    setState(() {
      _visibleProgress = true;
    });
  }

  void hideProgress() {
    setState(() {
      _visibleProgress = false;
    });
  }

  String latitude = '00.00000';
  String longitude = '00.00000';

  Future<String> getCountryName() async {
    Position position = await Geolocator.getCurrentPosition(
        desiredAccuracy: LocationAccuracy.high);
    debugPrint('location: ${position.latitude}');
    final coordinates =
        await new Coordinates(position.latitude, position.longitude);
    var addresses =
        await Geocoder.local.findAddressesFromCoordinates(coordinates);
    var first = addresses.first;
    print(
        "Country :${first.locality}, ${first.adminArea},${first.subLocality}, ${first.subAdminArea},${first.addressLine}, ${first.featureName},${first.thoroughfare}, ${first.subThoroughfare}");
    userData.Country = first.adminArea;
    userData.city = first.subAdminArea;
    userData.Latitude = position.latitude.toString();
    userData.Longitude = position.longitude.toString();

    return first.countryName; // this will return country name
  }
}
