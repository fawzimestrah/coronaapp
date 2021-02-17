import 'dart:core';
import 'package:flutter/material.dart';

class UserData{
  String _user_id="";
  String _email="";
  String _class_Type="C1";
  String _date_Register="";
  String _city="";
  String _Latitude="";
  String _Longitude="";
  String _Country="";
  String _username="";
  String _date_naissance="";
  String _sexe="";
  String _poids="";
  String _taille="";
  String _niveau_Education="";
  String _lieu_Resistance="";
  String _situation_Sociale="";
  String _nb_prs_famille="";
  String _nb_fils="";
  String _nb_fils_prs="";
  String _nb_chambres="";
  String _cas_professionnel="";
  String _specialiste_sante="";
  String _assurance="";
  String _suivi_recommandations="";
  String _alcool="";
  String _bolciguarette="";
  String _nb_ciguarette="";
  String _bolnarguile="";
  String _nb_Narguile="";



  Map<String, dynamic>  toJson() => {
  "user_id" :_user_id,
  "email" :_email,
  "class_Type":_class_Type,
  "date_Register":_date_Register,
  "city":_city,
  "Latitude":_Latitude,
  "Longitude":_Longitude,
  "Country":_Country,
  "username":_username,
  "date_naissance":_date_naissance,
  "sexe":_sexe,
  "poids":_poids,
  "taille":_taille,
  "niveau_Education":_niveau_Education,
  "lieu_Resistance":_lieu_Resistance,
  "situation_Sociale":_situation_Sociale,
  "nb_prs_famille":_nb_prs_famille,
  "nb_fils":_nb_fils,
  "nb_fils_prs":_nb_fils_prs,
  "nb_chambres":_nb_chambres,
  "cas_professionnel":_cas_professionnel,
  "specialiste_sante":_specialiste_sante,
  "assurance":_assurance,
  "suivi_recommandations":_suivi_recommandations,
  "alcool":_alcool,
  "bolciguarette":_bolciguarette,
  "nb_ciguarette":_nb_ciguarette,
  "bolnarguile":_bolnarguile,
  "nb_Narguile":_nb_Narguile

  };











  String get user_id => _user_id;

  set user_id(String value) {
    _user_id = value;
  }

  String get email => _email;

  set email(String value) {
    _email = value;
  }

  String get nb_Narguile => _nb_Narguile;

  set nb_Narguile(String value) {
    _nb_Narguile = value;
  }

  String get bolnarguile => _bolnarguile;

  set bolnarguile(String value) {
    _bolnarguile = value;
  }

  String get nb_ciguarette => _nb_ciguarette;

  set nb_ciguarette(String value) {
    _nb_ciguarette = value;
  }

  String get bolciguarette => _bolciguarette;

  set bolciguarette(String value) {
    _bolciguarette = value;
  }

  String get alcool => _alcool;

  set alcool(String value) {
    _alcool = value;
  }

  String get suivi_recommandations => _suivi_recommandations;

  set suivi_recommandations(String value) {
    _suivi_recommandations = value;
  }

  String get assurance => _assurance;

  set assurance(String value) {
    _assurance = value;
  }

  String get specialiste_sante => _specialiste_sante;

  set specialiste_sante(String value) {
    _specialiste_sante = value;
  }

  String get cas_professionnel => _cas_professionnel;

  set cas_professionnel(String value) {
    _cas_professionnel = value;
  }

  String get nb_chambres => _nb_chambres;

  set nb_chambres(String value) {
    _nb_chambres = value;
  }

  String get nb_fils_prs => _nb_fils_prs;

  set nb_fils_prs(String value) {
    _nb_fils_prs = value;
  }

  String get nb_fils => _nb_fils;

  set nb_fils(String value) {
    _nb_fils = value;
  }

  String get nb_prs_famille => _nb_prs_famille;

  set nb_prs_famille(String value) {
    _nb_prs_famille = value;
  }

  String get situation_Sociale => _situation_Sociale;

  set situation_Sociale(String value) {
    _situation_Sociale = value;
  }

  String get lieu_Resistance => _lieu_Resistance;

  set lieu_Resistance(String value) {
    _lieu_Resistance = value;
  }

  String get niveau_Education => _niveau_Education;

  set niveau_Education(String value) {
    _niveau_Education = value;
  }

  String get taille => _taille;

  set taille(String value) {
    _taille = value;
  }

  String get poids => _poids;

  set poids(String value) {
    _poids = value;
  }

  String get sexe => _sexe;

  set sexe(String value) {
    _sexe = value;
  }

  String get date_naissance => _date_naissance;

  set date_naissance(String value) {
    _date_naissance = value;
  }

  String get username => _username;

  set username(String value) {
    _username = value;
  }

  String get Country => _Country;

  set Country(String value) {
    _Country = value;
  }

  String get Longitude => _Longitude;

  set Longitude(String value) {
    _Longitude = value;
  }

  String get Latitude => _Latitude;

  set Latitude(String value) {
    _Latitude = value;
  }

  String get city => _city;

  set city(String value) {
    _city = value;
  }

  String get date_Register => _date_Register;

  set date_Register(String value) {
    _date_Register = value;
  }

  String get class_Type => _class_Type;

  set class_Type(String value) {
    _class_Type = value;
  }


}