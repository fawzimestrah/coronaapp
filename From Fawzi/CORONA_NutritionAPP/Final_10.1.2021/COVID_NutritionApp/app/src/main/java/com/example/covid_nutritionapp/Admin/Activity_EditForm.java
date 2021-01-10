package com.example.covid_nutritionapp.Admin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_nutritionapp.Data_GroupItem;
import com.example.covid_nutritionapp.Data_Question;
import com.example.covid_nutritionapp.Data_forms;
import com.example.covid_nutritionapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.text.TextUtils.isEmpty;

public class Activity_EditForm extends AppCompatActivity {

    EditText Ename, Edesc, EGroup;

    Button Bedit, Bnext, Bprev, Bdelete;
    ImageView imgdetails;
    FirebaseDatabase database;
    DatabaseReference myRef, myRefDetails;
    Data_forms currentForm;
    String keyForm;
    Bundle extra;

    private ArrayList<Data_Question> listQuest;
    TextView textQues;
    private EditText valueQuest;
    private LinearLayout viewQuest;
    private ArrayList<LinearLayout> listCurrentView;
    private ArrayList<EditText> listDataValueQuest;
    private ArrayList<ArrayList<AutoCompleteTextView>> listDataRadio;
    private ArrayList<Data_GroupADMIN> listGroup;
    private ArrayList<String> listnamegroup;
    private ArrayAdapter mArrayAdapterGroup, mArrayAdapterTypeQuest, mArrayAdapterLanguage;
    private String[] listTypeQuest;
    int c = 0, nbQues = 0, nbQuestSaved = 0;
    private FirebaseUser user;
    private Spinner mSpinner, mSpinnerQuest, mSpinnerLanguage;
    private String language = "";
    private ArrayList<String> listAutotCompleteRadio;
    private ArrayAdapter mArrayAdapterAutuComp;
    private ProgressBar mProgressBar;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_edit_form_layout);


        user = FirebaseAuth.getInstance().getCurrentUser();
        imgdetails = (ImageView) findViewById(R.id.imageviewdetails);
        Ename = findViewById(R.id.IdEname);
        Edesc = findViewById(R.id.IdEdesc);
        Edesc.setVisibility(View.GONE);
        Bedit = findViewById(R.id.IdBEdit);
        Bnext = findViewById(R.id.Idnext);
        Bprev = findViewById(R.id.IdPrev);
        viewQuest = findViewById(R.id.IdEQues);
        EGroup = findViewById(R.id.IdGroup);
        textQues = findViewById(R.id.IdTQues);
        Bdelete = findViewById(R.id.IdBdelete);
        mSpinner = findViewById(R.id.IdSpinnernamegroup);
        mSpinnerQuest = findViewById(R.id.IdmSpinnerQuest);
        mSpinnerLanguage = (Spinner) findViewById(R.id.idSpinnerLanguage);
        mProgressBar = findViewById(R.id.progressBar);
        listQuest = new ArrayList<Data_Question>();
        mSpinner.setEnabled(false);
        listDataValueQuest = new ArrayList<EditText>();
        listDataRadio = new ArrayList<ArrayList<AutoCompleteTextView>>();
        listCurrentView = new ArrayList<LinearLayout>();
        listGroup = new ArrayList<Data_GroupADMIN>();
        listnamegroup = new ArrayList<String>();
        listAutotCompleteRadio = new ArrayList<String>();

        Ename.setEnabled(false);
        Edesc.setEnabled(false);
        mArrayAdapterAutuComp = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, listAutotCompleteRadio);


        listTypeQuest = getResources().getStringArray(R.array.listTypeQuestion);

        hideSoftKeyboard();

        String[] listLanguage = getResources().getStringArray(R.array.languages);

        mArrayAdapterLanguage = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, listLanguage);
        if (mSpinnerLanguage != null) {
            mSpinnerLanguage.setAdapter(mArrayAdapterLanguage);

        }

        mSpinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listQuest.size() != 0) {
                    //save
                    listQuest.get(c).setQuestion(language, valueQuest.getText().toString());
                    listQuest.get(c).setGroup(EGroup.getText().toString());
                    saveDesc();
                    language = mSpinnerLanguage.getSelectedItem().toString();
                    showLayoutQuestion(c, listCurrentView);
                }
                // if listQuest.size==0 ==>
                language = mSpinnerLanguage.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mArrayAdapterTypeQuest = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, listTypeQuest);
        mArrayAdapterTypeQuest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (mSpinnerQuest != null) {
            mSpinnerQuest.setAdapter(mArrayAdapterTypeQuest);

        }


        mSpinnerQuest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listQuest.size() > 0) {
                    editLayoutQuestion(listTypeQuest[mSpinnerQuest.getSelectedItemPosition()], c);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        Bedit.setVisibility(View.INVISIBLE);

        extra = getIntent().getExtras();
        if (extra != null) {
            keyForm = extra.getString("keyForm");
            String fromActivity = extra.getString("fromActivity");
            if (fromActivity != null && fromActivity.equals("ADD")) {
                Bedit.setVisibility(View.VISIBLE);
            }
        }

        imgdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keyForm != null) {
                    Intent intent = new Intent(Activity_EditForm.this, Activity_ShowAllAnswers.class);
                    intent.putExtra("keyForm", keyForm);
                    startActivity(intent);
                }
            }
        });


        database = FirebaseDatabase.getInstance();

        myRef = database.getReference("FORMS");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showDialog();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Data_forms D = snapshot.getValue(Data_forms.class);
                    D.setKey_value(snapshot.getKey());
                    if (D.getKey_value().equals(keyForm)) {
                        currentForm = D;
                    }
                }
                Ename.setText(currentForm.getNameform());
                Edesc.setText(currentForm.getDescForm_lang(language));
                hideDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final DatabaseReference myRefG = database.getReference("GroupAdmin");
        myRefG.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listGroup.clear();
                listnamegroup.clear();
                showDialog();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //snapshot.getKey();
                    final Data_GroupADMIN D = snapshot.getValue(Data_GroupADMIN.class);
                    D.setKey_Group(snapshot.getKey());
                    DatabaseReference myRefUser = database.getReference("GroupAdmin").child(snapshot.getKey()).child("Members");
                    myRefUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                //snapshot.getKey();
                                String Dkey = snapshot.getValue(String.class);
                                if (Dkey.equals(user.getUid())) {
                                    listGroup.add(D);
                                    listnamegroup.add(D.getNameGroup());

                                }
                            }

                            String[] Array_namegroup = new String[listnamegroup.size()];
                            for (int i = 0; i < listnamegroup.size(); i++) {
                                Array_namegroup[i] = listnamegroup.get(i);
                            }
                            mArrayAdapterGroup = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, Array_namegroup);
                            mArrayAdapterGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            if (mSpinner != null) {
                                mSpinner.setAdapter(mArrayAdapterGroup);

                            }


                            if (currentForm != null && currentForm.getGroupCreator().equals(D.getKey_Group()) && listnamegroup.size() > 0) {
                                mSpinner.setSelection(listnamegroup.indexOf(D.getNameGroup()));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                hideDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRefDetails = database.getReference("FORMS");

        myRef.child(keyForm).child("QUESTIONS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listQuest.clear();
                nbQues = 0;
                c = 0;
                nbQuestSaved = 0;
                listCurrentView.clear();
                listDataRadio.clear();
                // viewQuest.removeAllViews();
                viewQuest.removeAllViewsInLayout();
                showDialog();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Data_GroupItem D = snapshot.getValue(Data_GroupItem.class);
                    D.setName_group(snapshot.getKey());
                    Log.e("namegroup", "grouup" + D.getName_group());

                    myRefDetails.child(keyForm).child("QUESTIONS").child(D.getName_group()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Data_Question Dq = snapshot.getValue(Data_Question.class);
                                Dq.setKey_value(snapshot.getKey());
                                Dq.setKey_form(keyForm);
                                Log.e("questiongrp", "grouup" + Dq.getKey_value());
                                if (Dq.isShared()) {
                                    listQuest.add(Dq);
                                    if (Dq.getTypeQuestion() != null) {
                                        createLayoutQuestion(Dq.getTypeQuestion());
                                    }
                                    nbQues++;
                                    nbQuestSaved++;
                                }
                            }
                            Log.e("Nbques1", nbQuestSaved + "<>" + nbQues);

                            showLayoutQuestion(c, listCurrentView);
                            hideDialog();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Bedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkField()) {
                    showDialog();
                    saveDesc();
                    if (listQuest.size() > 0) {
                        listQuest.get(c).setQuestion(language, valueQuest.getText().toString());
                        listQuest.get(c).setGroup(EGroup.getText().toString());
                    } // kermel e5r we7de
                    for (int h = 0; h < listQuest.size(); h++) {
                        saveDataradio(h);
                    }

                    Log.e("sizeedit=", "" + listQuest.size());
//                    String KeyGroup = listGroup.get(mSpinner.getSelectedItemPosition()).getKey_Group();
                    Bprev.setEnabled(false);
                    Bdelete.setEnabled(false);
                    Bnext.setEnabled(false);
                    Bedit.setEnabled(false);
                    Data_forms.Update_form(Activity_EditForm.this, currentForm.getKey_value(), Ename.getText().toString(), currentForm.getDateform(), Edesc.getText().toString(), currentForm.getDescformLang(), user.getUid(), currentForm.getGroupCreator(), listQuest);
                    finish();
                }
            }
        });


        Bdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_EditForm.this);
                builder.setTitle("DELETE");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("Delete FORM", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        Data_forms.deleteForm(keyForm);
                        Intent intent = new Intent(getApplicationContext(), Activity_MainAdmin.class);
                        setResult(1, intent);
                        finish();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Delete QUESTION", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listQuest.size() > 0) {
                            if (nbQuestSaved > c) {
                                Log.e("deleteindex:", "c=" + c + " // nbsaved=" + nbQuestSaved);
                                Data_Question.deleteQuestion(listQuest.get(c), Activity_EditForm.this);
                            }
                            listQuest.remove(c); //
                            listDataRadio.remove(c);
                            listCurrentView.remove(c);
                            Log.e("deleteindexno:", "c=" + c + " // nbsaved=" + nbQuestSaved);

                            if (c > 0) {
                                c--;
                            }
                            showLayoutQuestion(c, listCurrentView);
                            nbQues--;
                        }
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }


        });


        Bnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDesc();
                if (checkField()) {
                    if (listQuest.size() > c + 1) {
                        listQuest.get(c).setQuestion(language, valueQuest.getText().toString());
                        listQuest.get(c).setGroup(EGroup.getText().toString());
                        c++;
                        showLayoutQuestion(c, listCurrentView);

                    } else {
                        listQuest.get(c).setQuestion(language, valueQuest.getText().toString());
                        listQuest.get(c).setGroup(EGroup.getText().toString());
                        // hon case krmel spinner
                        c++;
                        next_Quest(c);
                        valueQuest.setText("");

                    }
                    textQues.setText("Question " + (c + 1) + "/" + nbQues + ":");
                    valueQuest.setFocusable(true);
                }
            }
        });


        Bprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDesc();
                if (listQuest.size() == c + 1) {
                    listQuest.get(c).setQuestion(language, valueQuest.getText().toString());
                    listQuest.get(c).setGroup(EGroup.getText().toString());
                }
                if (c > 0) {
                    listQuest.get(c).setQuestion(language, valueQuest.getText().toString());
                    listQuest.get(c).setGroup(EGroup.getText().toString());
                    EGroup.setText(listQuest.get(c).getGroup());
                    c--;
                    showLayoutQuestion(c, listCurrentView);
                    valueQuest.setText(listQuest.get(c).getQuestion_lang(language));
                    EGroup.setText(listQuest.get(c).getGroup());
                    textQues.setText("Question " + (c + 1) + "/" + nbQues + ":");
                }

            }

        });


    }


    public void next_Quest(int index) {
        nbQues++;
        Data_Question D1 = new Data_Question("EN", "");
        D1.addLanguage_Question("AR", "");
        D1.setGroup("");
        D1.setKey_value(null);
        D1.setNum_Item(nbQues);
        //if(mSpinnerQuest.getSelectedItemPosition()>=0) {
        D1.setTypeQuestion(listTypeQuest[0]);// TextField
        mSpinnerQuest.setSelection(0);
        //}
        listQuest.add(D1);
        createLayoutQuestion("TextField");

    }


    public void saveDataradio(int h) {
        if (listDataRadio.size() > h && listDataRadio.get(h) != null) {
            listQuest.get(h).getChoix().clear();
            for (int j = 0; j < listDataRadio.get(h).size(); j++) {
                if (!listDataRadio.get(h).get(j).getText().toString().equals("") && !listDataRadio.get(h).get(j).getText().toString().equals(" ")) {
                    listQuest.get(h).AddChoix(listDataRadio.get(h).get(j).getText().toString());
                    if (!listAutotCompleteRadio.contains(listDataRadio.get(h).get(j).getText().toString())) {
                        listAutotCompleteRadio.add(listDataRadio.get(h).get(j).getText().toString());
                    }
                }
            }

        }


    }

    public void showLayoutQuestion(int index, ArrayList<LinearLayout> listlayout) {
        showDialog();
        saveDataradio(index);
        try {
            //  viewQuest.removeAllViews();
            viewQuest.removeAllViewsInLayout();

        } catch (Exception e) {
            //       restartActivity(this);
            Log.e("testdebug", "exception try viewQuest");
            e.printStackTrace();

        }

        valueQuest = listDataValueQuest.get(index); // valueQuest ==field current
        valueQuest.setText(listQuest.get(index).getQuestion_lang(language));
        valueQuest.setFocusable(true);
        viewQuest.addView(listCurrentView.get(index));
        EGroup.setText(listQuest.get(index).getGroup());
        if (!language.equals("") && currentForm != null) {
            Edesc.setText(currentForm.getDescForm_lang(language));
        }
        switch (listQuest.get(index).getTypeQuestion()) {
            case ("TextField"):
                mSpinnerQuest.setSelection(0);
                break;
            case ("RadioButton"):

                mSpinnerQuest.setSelection(1);
                break;
            case ("CheckBox"):
                mSpinnerQuest.setSelection(2);
                break;
            case ("AutoComplete"):
                mSpinnerQuest.setSelection(3);
                break;
            case ("Spinner"):
                mSpinnerQuest.setSelection(4);
        }

        hideDialog();
    }


    @SuppressLint("ResourceAsColor")
    public void createLayoutQuestion(String TypeQuestion) {
        final LinearLayout linearCurrent;
        switch (TypeQuestion) {
            case "TextField":
                //viewQuest.removeAllViews();
                viewQuest.removeAllViewsInLayout();
                linearCurrent = new LinearLayout(getApplicationContext());
                linearCurrent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                linearCurrent.setOrientation(LinearLayout.VERTICAL);
                valueQuest = new EditText(getApplicationContext());
                valueQuest.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                valueQuest.setTextColor(R.color.colorPrimaryDark);
                valueQuest.setHint("Enter Text Question ");
                if (listQuest.size() >= nbQues + 1) { // last index in list
                    valueQuest.setText(listQuest.get(nbQues).getQuestion_lang(language));

                }
                linearCurrent.addView(valueQuest);
                // add question to list question  and radio null  to list radio
                listDataValueQuest.add(valueQuest);
                listDataRadio.add(null);
                viewQuest.addView(linearCurrent);
                listCurrentView.add(linearCurrent); // list
                break;

            case "RadioButton":
            case "CheckBox":
            case "AutoComplete":
            case "Spinner":
                try {
                    //   viewQuest.removeAllViews();
                    viewQuest.removeAllViewsInLayout();
                } catch (Exception e) {
                }
                linearCurrent = new LinearLayout(getApplicationContext());
                linearCurrent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                linearCurrent.setOrientation(LinearLayout.VERTICAL);
                valueQuest = new EditText(getApplicationContext());
                valueQuest.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                valueQuest.setTextColor(R.color.colorPrimaryDark);
                valueQuest.setHint("Enter Text Question ");
                linearCurrent.addView(valueQuest);
                valueQuest.setText(listQuest.get(nbQues).getQuestion_lang(language));
                //done
                LinearLayout LAdd = new LinearLayout(getApplicationContext());
                LAdd.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                LAdd.setOrientation(LinearLayout.HORIZONTAL);

                final ImageView imadd = new ImageView(getApplicationContext());
                imadd.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                imadd.setImageResource(R.drawable.ic_add_24dp);
                LAdd.addView(imadd);
                linearCurrent.addView(LAdd);
                listCurrentView.add(linearCurrent); // list

                final ArrayList<AutoCompleteTextView> currentRadio = new ArrayList<AutoCompleteTextView>();

                imadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        LinearLayout LRadio1 = new LinearLayout(getApplicationContext());
                        LRadio1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        LRadio1.setOrientation(LinearLayout.HORIZONTAL);
                        final AutoCompleteTextView Eradio1 = new AutoCompleteTextView(getApplicationContext());
                        Eradio1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        Eradio1.setTextColor(R.color.colorPrimaryDark);
                        Eradio1.setHintTextColor(R.color.design_default_color_primary_dark);
                        if (Eradio1 != null) {
                            Eradio1.setAdapter(mArrayAdapterAutuComp);
                        }
                        Eradio1.setHint("Option");

                        // add to list
                        LRadio1.addView(Eradio1);
                        currentRadio.add(Eradio1);
                        //add view
                        linearCurrent.addView(LRadio1);
                    }
                });

                for (int i = 0; i < listQuest.get(nbQues).getChoix().size(); i++) { // nbquest-1 la eno heye e5r question bl list
                    LinearLayout linearRadio = new LinearLayout(getApplicationContext());
                    linearRadio.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    linearRadio.setOrientation(LinearLayout.VERTICAL);
                    final AutoCompleteTextView Eradio = new AutoCompleteTextView(getApplicationContext());
                    Eradio.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    Eradio.setTextColor(R.color.colorPrimaryDark);
                    Eradio.setHintTextColor(R.color.design_default_color_primary_dark);
                    Eradio.setText(listQuest.get(nbQues).getChoix().get(i));
                    if (Eradio != null) {
                        Eradio.setAdapter(mArrayAdapterAutuComp);
                    }
                    Eradio.setHint("Option");

                    currentRadio.add(Eradio);
                    linearRadio.addView(Eradio);
                    // LRadio.addView(imAdd);
                    linearCurrent.addView(linearRadio);
                }


                // add question to list question  and radio null  to list radio
                listDataValueQuest.add(valueQuest);
                viewQuest.addView(linearCurrent);

                listDataRadio.add(currentRadio);
                break;

            default:
                Toast.makeText(getApplicationContext(), "default", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @SuppressLint("ResourceAsColor")
    public void editLayoutQuestion(String TypeQuestion, int index) {
        final LinearLayout linearcurrent;
        if (valueQuest != null) {
            //save  question
            listQuest.get(index).setQuestion(language, valueQuest.getText().toString());
            listQuest.get(index).setGroup(EGroup.getText().toString());
            listQuest.get(index).setTypeQuestion(TypeQuestion); // set Type Question

            // viewQuest.removeAllViews();
            viewQuest.removeAllViewsInLayout();
            listCurrentView.get(index).removeAllViews();
        }
        switch (TypeQuestion) {
            case "TextField":
                linearcurrent = new LinearLayout(getApplicationContext());
                linearcurrent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                linearcurrent.setOrientation(LinearLayout.VERTICAL);
                valueQuest = new EditText(getApplicationContext());
                valueQuest.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                valueQuest.setTextColor(R.color.colorPrimaryDark);
                valueQuest.setText(listQuest.get(index).getQuestion_lang(language));
                linearcurrent.addView(valueQuest); // valueQuest meme
                viewQuest.addView(linearcurrent);
                //add to view
                listCurrentView.remove(index);
                listCurrentView.add(index, linearcurrent); // list
                // add question to list question  and radio null  to list radio\
                listDataValueQuest.remove(index);
                listDataValueQuest.add(index, valueQuest);
                // radio null (textfield)
                listDataRadio.remove(c);
                listDataRadio.add(c, null);
                break;
            case "RadioButton":
            case "CheckBox":
            case "AutoComplete":
            case "Spinner":
                linearcurrent = new LinearLayout(getApplicationContext());
                linearcurrent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                linearcurrent.setOrientation(LinearLayout.VERTICAL);
                valueQuest = new EditText(getApplicationContext());
                valueQuest.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                valueQuest.setTextColor(R.color.colorPrimaryDark);
                valueQuest.setText(listQuest.get(c).getQuestion_lang(language));
                //Toast.makeText(getApplicationContext(),"in edit :"+valueQuest.getText().toString(),Toast.LENGTH_LONG).show();
                linearcurrent.addView(valueQuest);// valueQuest meme
                final ArrayList<AutoCompleteTextView> currentRadio = new ArrayList<AutoCompleteTextView>();
                LinearLayout LAdd = new LinearLayout(getApplicationContext());
                LAdd.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                LAdd.setOrientation(LinearLayout.HORIZONTAL);


                final ImageView imadd = new ImageView(getApplicationContext());
                imadd.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                imadd.setImageResource(R.drawable.ic_add_24dp);
                LAdd.addView(imadd);
                linearcurrent.addView(LAdd);
                listDataRadio.add(currentRadio);

                if (listQuest.get(index).getChoix().size() != 0) {
                    for (int i = 0; i < listQuest.get(index).getChoix().size(); i++) { // nbquest-1 la eno heye e5r question bl list
                        LinearLayout linearRadio = new LinearLayout(getApplicationContext());
                        linearRadio.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        linearRadio.setOrientation(LinearLayout.VERTICAL);
                        final AutoCompleteTextView Eradio = new AutoCompleteTextView(getApplicationContext());
                        Eradio.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        Eradio.setTextColor(R.color.colorPrimaryDark);
                        Eradio.setHint("Enter Text RadioButton1");
                        if (Eradio != null) {
                            Eradio.setAdapter(mArrayAdapterAutuComp);
                        }
                        Eradio.setHint("Option");

                        Eradio.setHintTextColor(R.color.design_default_color_primary_dark);
                        // Toast.makeText(getApplicationContext(),"i="+i,Toast.LENGTH_LONG).show();
                        Eradio.setText(listQuest.get(index).getChoix().get(i));
                        currentRadio.add(Eradio);
                        linearRadio.addView(Eradio);
                        // LRadio.addView(imAdd);
                        linearcurrent.addView(linearRadio);

                    }
                }


                imadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        LinearLayout LRadio1 = new LinearLayout(getApplicationContext());
                        LRadio1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        LRadio1.setOrientation(LinearLayout.HORIZONTAL);
                        final AutoCompleteTextView Eradio1 = new AutoCompleteTextView(getApplicationContext());
                        Eradio1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        Eradio1.setTextColor(R.color.colorPrimaryDark);
                        Eradio1.setHint("Enter Text RadioButton");
                        Eradio1.setHintTextColor(R.color.design_default_color_primary_dark);
                        if (Eradio1 != null) {
                            Eradio1.setAdapter(mArrayAdapterAutuComp);
                        }
                        Eradio1.setHint("Option");
                        // add to list
                        LRadio1.addView(Eradio1);
                        currentRadio.add(Eradio1);
                        //add view
                        linearcurrent.addView(LRadio1);
                    }
                });
                viewQuest.addView(linearcurrent);
                // question
                listDataValueQuest.remove(c);
                listDataValueQuest.add(c, valueQuest);
                // add view
                listCurrentView.remove(c);
                listCurrentView.add(c, linearcurrent); // list

                // add question to list question  and radio null  to list radio
                listDataValueQuest.remove(index);
                listDataValueQuest.add(index, valueQuest);
                //add radio
                listDataRadio.remove(c);
                listDataRadio.add(c, currentRadio);
                break;
            default:
                Toast.makeText(getApplicationContext(), "default", Toast.LENGTH_LONG).show();
                break;
        }


    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    private boolean checkField() {
        if (isEmpty(Ename.getText().toString()) ||
                // isEmpty(Edesc.getText().toString()) ||
                isEmpty(EGroup.getText().toString())) {
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "You didn't fill in all the fields (Name form , description , group). ", Snackbar.LENGTH_SHORT).show();

            return false;
        }
        if (isEmpty(valueQuest.getText().toString())) {
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "You didn't fill in all the fields (Question). ", Snackbar.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }


    private void saveDesc() {
        if (currentForm != null && !language.equals("")) {
            currentForm.setDescform(language, Edesc.getText().toString());
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }


    private void showDialog() {
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void restartActivity(Activity activity) {
        activity.finish();
        activity.startActivity(activity.getIntent());

    }


}
