package com.example.lloyd.practice;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;

import static com.example.lloyd.practice.MainActivity.IMAGES_VIEW;
import static com.example.lloyd.practice.MainActivity.PHONEBOOK_VIEW;
import static com.example.lloyd.practice.MainActivity.SMS_VIEW;
import static com.example.lloyd.practice.MainActivity.contacts;
import static com.example.lloyd.practice.MainActivity.imgCursor;
import static com.example.lloyd.practice.MainActivity.sms_s;

public class ActivityViewDataList extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //handle incomming phonebook intent

        if(getIntent().getExtras().getInt("reqCode") == PHONEBOOK_VIEW) {
            arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, contacts);
            listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(arrayAdapter);
        }
        else
           if(getIntent().getExtras().getInt("reqCode") == SMS_VIEW) {
               arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, sms_s);
               listView = (ListView) findViewById(R.id.listView);
               listView.setAdapter(arrayAdapter);
           }
           else
           if(getIntent().getExtras().getInt("reqCode") == IMAGES_VIEW) {

            cursorAdaptor c = new cursorAdaptor(getApplicationContext(),imgCursor,true);
               listView = (ListView) findViewById(R.id.listView);
               listView.setAdapter(c);
           }



    }

}
