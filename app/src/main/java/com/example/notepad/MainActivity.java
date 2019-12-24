package com.example.notepad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    static ArrayList<String> notes;
    static ArrayAdapter arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        //apparently doesn't exist
        menuInflater.inflate(R.menu.add_note_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        if(item.getItemId()== R.id.add_note){
            Intent intent = new Intent(getApplicationContext(),NoteEditor.class);

            startActivity(intent);

            return true;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        notes = new ArrayList<>();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notepad", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes",null);
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,notes);

        if(set == null){
            notes.add("example");
        }else{
            notes = new ArrayList<>(set);
        }
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                Intent intent = new Intent(getApplicationContext(),NoteEditor.class);
                intent.putExtra("noteID",i);
                startActivity(intent);
            }
        });

        listView.setOnLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //apparently needs to be final but that causes errors
                int itemToDelete = position;
                new AlertDialog.Builder(MainActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Are you sure?")
                    .setMessage("do you want to delete this note?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            notes.remove(itemToDelete);
                            arrayAdapter.notifyDataSetChanged();

                            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notepad", Context.MODE_PRIVATE);

                            HashSet<String> set = new HashSet<>(MainActivity.notes);
                            sharedPreferences.edit().putStringSet("notes",set).apply();
                        }
                    })
                    .setNegativeButton("No",null);
            return true;

            }
        });
    }
}
