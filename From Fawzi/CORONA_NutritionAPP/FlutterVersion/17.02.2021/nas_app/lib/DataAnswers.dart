import 'dart:core';
import 'package:flutter/material.dart';

class DataAnswers {

  String _key_form;
  String _key_value = null;
  String _key_value_Quest = null;
  String _user;
  String _question;

  String _answer = "";
  int _num_Item;
  String _group;

  Map<int,String> grpAnswers;


  DataAnswers(
      this._key_form,
      this._key_value,
      this._key_value_Quest,
      this._user,
      this._question,
      this._answer,
      this._num_Item,
      this._group,
      );
//  {_key_value}

  Map<String, dynamic>  toJson() => {
    "key_form"   :_key_form,
    "key_value" : _key_value,
    "question" : _question,
    "num_Item"      : _num_Item,
    "group": _group,
    "user": _user,
    "answer": _answer
  };

  factory DataAnswers.fromJson(Map<String, dynamic> json) {
    return new DataAnswers(
      json["key_form"],
       json["key_value"],
       null,
        json["user"],
        json["question"],
        json["num_Item"],
        json["group"],
         json["answer"]
//        products: json['products'] != null ? new List<CartItem>.from(
//          json['products'].map((x) => CartItem.fromJson(x))) : List<CartItem>(),
    );
  }

  String get key_value => _key_value;

  set key_value(String value) {
    _key_value = value;
  }

  String get key_value_Quest => _key_value_Quest;

  set key_value_Quest(String value) {
    _key_value_Quest = value;
  }

  String get user => _user;

  set user(String value) {
    _user = value;
  }

  String get question => _question;

  set question(String value) {
    _question = value;
  }

  String get answer => _answer;

  set answer(String value) {
    _answer = value;
  }

  int get num_Item => _num_Item;

  set num_Item(int value) {
    _num_Item = value;
  }

  String get group => _group;

  set group(String value) {
    _group = value;
  }
  String get key_form => _key_form;

  set key_form(String value) {
    _key_form = value;
  }

}

