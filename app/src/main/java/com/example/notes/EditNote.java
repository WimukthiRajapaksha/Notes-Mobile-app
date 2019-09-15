package com.example.notes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.util.HashSet;

public class EditNote extends AppCompatActivity implements TextWatcher {
    int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText text = findViewById(R.id.editText);
        Intent inte = getIntent();
        noteId = inte.getIntExtra("noteId",-1);
        text.setText(MainActivity.list.get(noteId));
        text.addTextChangedListener( this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        MainActivity.list.set(noteId, String.valueOf(charSequence));
        MainActivity.adapter.notifyDataSetChanged();
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
        if (MainActivity.set != null){
            MainActivity.set.clear();
        } else {
            MainActivity.set = new HashSet<String>();
        }

        MainActivity.set.addAll(MainActivity.list);
        sharedPreferences.edit().remove("note").apply();
        sharedPreferences.edit().putStringSet("note", MainActivity.set).apply();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
