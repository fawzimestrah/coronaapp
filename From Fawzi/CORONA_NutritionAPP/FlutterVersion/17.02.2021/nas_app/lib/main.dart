import 'package:flutter/material.dart';

import 'package:firebase_auth/firebase_auth.dart';

import 'package:firebase_core/firebase_core.dart';

import 'About.dart';
import 'Client/mainClient.dart';
import 'Client/registerClient.dart';



final FirebaseAuth _auth = FirebaseAuth.instance;
final TextEditingController _emailController = TextEditingController();
final TextEditingController _passwordController = TextEditingController();
final GlobalKey<ScaffoldState> scaffoldState = new GlobalKey<ScaffoldState>();

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp().whenComplete(() {
    print("complete initialiseApp() ");
  });
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> with WidgetsBindingObserver {
  @override
  void initState() {
    // TODO: implement initState

    super.initState();
    _signInWithEmailAndPassword();
  }

//  @override
//  void dispose() {
//    _emailController.dispose();
//    _passwordController.dispose();
//    super.dispose();
//  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    if (state == AppLifecycleState.resumed) {
      // user returned to our app
      _signInWithEmailAndPassword();
    } else if (state == AppLifecycleState.inactive) {
      // app is inactive
    } else if (state == AppLifecycleState.paused) {
      // user is about quit our app temporally
    }
  }

  Future<void> _login() async {
    _signInWithEmailAndPassword();
  }

  toAbout(){
     Navigator.push(
        context,
        MaterialPageRoute(
          builder: (context) => About(),
        ));
  }
  @override
  Widget build(BuildContext context) {
    TextStyle style = TextStyle(fontFamily: 'Montserrat', fontSize: 20.0);

    final emailField = TextField(
      controller: _emailController,
      obscureText: false,
      style: style,
      decoration: InputDecoration(
          contentPadding: EdgeInsets.fromLTRB(20.0, 15.0, 20.0, 15.0),
          hintText: "Email",
          border:
          OutlineInputBorder(borderRadius: BorderRadius.circular(20.0))),
    );
    final passwordField = TextField(
      controller: _passwordController,
      obscureText: true,
      style: style,
      decoration: InputDecoration(
          contentPadding: EdgeInsets.fromLTRB(20.0, 15.0, 20.0, 15.0),
          hintText: "Password",
          border:
          OutlineInputBorder(borderRadius: BorderRadius.circular(20.0))),
    );
    final loginButon = Material(
      elevation: 5.0,
      borderRadius: BorderRadius.circular(30.0),
      color: Colors.lightBlue,
      child: MaterialButton(
        minWidth: MediaQuery.of(context).size.width,
        padding: EdgeInsets.fromLTRB(20.0, 15.0, 20.0, 15.0),
        onPressed: () => _login(),
        child: Text("Login",
            textAlign: TextAlign.center,
            style: style.copyWith(
                color: Colors.white, fontWeight: FontWeight.bold)),
      ),
    );

    final registerButon = Material(
      elevation: 5.0,
      borderRadius: BorderRadius.circular(30.0),
      color: Colors.lightBlue,
      child: MaterialButton(
        minWidth: MediaQuery.of(context).size.width,
        padding: EdgeInsets.fromLTRB(20.0, 15.0, 20.0, 15.0),
        onPressed: () => _registerAccount(),
        child: Text("Register",
            textAlign: TextAlign.center,
            style: style.copyWith(
                color: Colors.white, fontWeight: FontWeight.bold)),
      ),
    );

    var cardLogo=   new Container(
      margin: EdgeInsets.fromLTRB(16, 4, 16, 4),
      color: Colors.white,
      child: Image.asset('assets/logosnasul.png'),
    );

    return new Scaffold(
        key: scaffoldState,
        appBar: new AppBar(
          title: new Text("Login"),
        ),
        body: new SingleChildScrollView(
          padding: EdgeInsets.all(16),
          child: new Center(
            child: new Container(
              color: Colors.white70,
              child: new Padding(
                padding: EdgeInsets.all(16.0),
                child: new Column(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: <Widget>[
                    SizedBox(
                      height: 155.0,
                      child: new Icon(
                        Icons.person,
                      ),
                    ),
                    SizedBox(height: 45.0),
                    emailField,
                    SizedBox(height: 25.0),
                    passwordField,
                    SizedBox(
                      height: 35.0,
                    ),
                    loginButon,
                    SizedBox(
                      height: 15.0,
                    ),
                    registerButon,
                    SizedBox(
                      height: 15.0,
                    ),
                    new FlatButton(
                        onPressed: toAbout,
                        child:new Text("About",
                        style: style.copyWith(color: Colors.lightBlueAccent)) ),
                    cardLogo,
                  ],
                ),
              ),
            ),
          ),
        ));
  }

  void _registerAccount() async {
    final result = await Navigator.push(
        context,
        MaterialPageRoute(
          builder: (context) => register(),
        ));
    print("register");

    final User user = (await _auth.createUserWithEmailAndPassword(
      email: _emailController.text,
      password: _passwordController.text,
    ))
        .user;

//    if (user != null) {
//      if (!user.emailVerified) {
//        await user.sendEmailVerification();
//      }
//      await user.updateProfile(displayName: "fmm1");
//      final user1 = _auth.currentUser;
//      print(" ok register");
//
//      Navigator.of(context).pushReplacement(MaterialPageRoute(
//          builder: (context) => MainClient(
//              //      user: user1,
//              )));
//    } else {
////      _isSuccess = false;
//      print("else register ");
//    }
  }

  void _signInWithEmailAndPassword() async {
    try {
      User user;
      String uid = null;
      user = await _auth.currentUser;
      if (user != null) {
        uid = await user.uid;
      }

      if (uid != null) {
        await SnackDialog("Authenticated with  ${_auth.currentUser.email}");
        await print("Authenticated with  ${_auth.currentUser.email}");

        Navigator.of(context).pushReplacement(MaterialPageRoute(builder: (_) {
          return MainClient();
        }));
      }

      user = (await _auth.signInWithEmailAndPassword(
        email: _emailController.text + "@CovidNutrUl.com",
        password: _passwordController.text,
      ))
          .user;

      if (!user.emailVerified) {
        await user.sendEmailVerification();
      }
      await SnackDialog("Authenticated with  ${_auth.currentUser.email}");
      await print("Authenticated with  ${_auth.currentUser.email}");
      Navigator.pop(context);
      Navigator.of(context).push(MaterialPageRoute(builder: (_) {
        return MainClient();
        //   user: user,
        //);
      }));
    } catch (e) {
      SnackDialog("$e");
      print("else signIn");
    }
  }

  void SnackDialog(message) {
    scaffoldState.currentState
        .showSnackBar(new SnackBar(content: new Text(message)));
  }
}
