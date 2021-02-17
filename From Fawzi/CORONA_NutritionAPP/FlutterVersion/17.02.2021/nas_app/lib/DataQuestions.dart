class DataQuestions {

  String _key_value = null;
  String _key_form = null;
  Map<String, String> _questions;
  String _question = "";
  int _num_Item = 0;
  String _group = "Default";
  String _typeQuestion = "";
  bool _isShared = true;
  Map<dynamic, dynamic> _choix;

  DataQuestions(this._key_value, this._key_form, this._questions, this._question,
      this._num_Item, this._group, this._typeQuestion, this._isShared, this._choix);


  factory DataQuestions.fromJson(Map<String, dynamic> json) {
    return new DataQuestions(
        json["key_value"],
        json["key_form"],
        json["questions"],
        json["question"],
        json["num_Item"],
        json["group"],
        json["typeQuestion"],
        json["shared"],
        json["choix"]);
//        products: json['products'] != null ? new List<CartItem>.from(
//          json['products'].map((x) => CartItem.fromJson(x))) : List<CartItem>(),

  }

  Map<dynamic, dynamic> get choix => _choix;

  set choix(Map<dynamic, dynamic> value) {
    _choix = value;
  }

  bool get isShared => _isShared;

  set isShared(bool value) {
    _isShared = value;
  }

  String get typeQuestion => _typeQuestion;

  set typeQuestion(String value) {
    _typeQuestion = value;
  }

  String get group => _group;

  set group(String value) {
    _group = value;
  }

  int get num_Item => _num_Item;

  set num_Item(int value) {
    _num_Item = value;
  }

  String get question => _question;

  set question(String value) {
    _question = value;
  }

  Map<String, String> get questions => _questions;

  set questions(Map<String, String> value) {
    _questions = value;
  }

  String get key_form => _key_form;

  set key_form(String value) {
    _key_form = value;
  }

  String get key_value => _key_value;

  set key_value(String value) {
    _key_value = value;
  }




}
