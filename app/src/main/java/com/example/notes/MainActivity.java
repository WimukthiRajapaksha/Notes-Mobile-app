package com.example.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> list = new ArrayList<>();
    static ArrayAdapter adapter;
    static Set<String> set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView = findViewById(R.id.listView);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
        set = sharedPreferences.getStringSet("note", null);
        list.clear();

        if (set != null) {
            list.addAll(set);
        } else {
            list.add("Example Note 01");
            set=new HashSet<String>();
            set.addAll(list);
            sharedPreferences.edit().remove("note").apply();
            sharedPreferences.edit().putStringSet("note", set).apply();
//            Log.i("Here", "Nothing");
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);

        listView.setAdapter(adapter);

        listView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                list.remove(i);
                                SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);

                                if (set != null){
                                    set.clear();
                                } else {
                                    set = new HashSet<String>();
                                }

                                set.addAll(list);
                                sharedPreferences.edit().remove("note").apply();
                                sharedPreferences.edit().putStringSet("note", set).apply();
                                adapter.notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
//                return true;
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("Here", "short click");
                Intent in = new Intent(getApplicationContext(), EditNote.class);
                in.putExtra("noteId", i);
                startActivity(in);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.addBtn) {
            list.add("");

            if (set != null){
                set.clear();
            } else {
                set = new HashSet<String>();
            }

            SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
            set.addAll(list);
            sharedPreferences.edit().remove("note").apply();
            sharedPreferences.edit().putStringSet("note", set).apply();
            adapter.notifyDataSetChanged();

            Intent in = new Intent(getApplicationContext(), EditNote.class);
            in.putExtra("noteId", list.size() -1 );
            startActivity(in);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
