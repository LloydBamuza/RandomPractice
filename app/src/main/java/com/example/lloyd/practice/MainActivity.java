package com.example.lloyd.practice;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static Uri imageDirUri;
    static Context appContext = null;
    Button btn,btn2,btn3,btn4,btn5,btn6,btn7,btn8;
    public static ArrayList<String> contacts = new ArrayList<>();
    public static ArrayList<String> sms_s = new ArrayList<>();
    static public Cursor imgCursor;
    public static final int PHONEBOOK_VIEW = 100;
    public static final int SMS_VIEW = 200;
    public static final int IMAGES_VIEW = 300;
    final int NOTOF_ACTION = 808;

    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        });

        btn = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn6 = (Button) findViewById(R.id.btn6);
        btn7 = (Button) findViewById(R.id.btn7);
        btn8 = (Button) findViewById(R.id.btn8);
        appContext = getApplicationContext();


        //Notification displaying button
        btn.setOnClickListener(e->{
            //create inboxstyle notification

            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                    .setBigContentTitle("Big Title")
                    .addLine("This is line: 1 ")
                    .addLine("This is line: 2 ")
                    .addLine("This is line: 3 ")
                    .addLine("This is line: 4 ")
                    .addLine("This is line: 5 ")
                    .setSummaryText("Summary");

            NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext(), " ")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setColor(Color.RED)
                    .setContentTitle("Builder Title")
                    .setContentInfo("Builder content info")
                    .setStyle(inboxStyle);

            //Intent and pending intent for notification button
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),NOTOF_ACTION,intent,0);

            //Add notification action button
            builder.addAction(R.drawable.ic_launcher_background,"Click me",pendingIntent);

            //Get notification manager, build and set notification
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Toast.makeText(getApplicationContext(),"Working",Toast.LENGTH_LONG).show();
            notificationManager.notify(0,builder.build());

        });

        //Button click get list of contacts
        btn2.setOnClickListener(e->{

            //request contacts permission
            ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.READ_CONTACTS},8);

            ArrayAdapter<String> arrayAdapter;
            Cursor cursor, cursorNum= null;


            Uri contactsDirUri = ContactsContract.Contacts.CONTENT_URI;
            cursor = getContentResolver().query(contactsDirUri, new String[]
                    {ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER,ContactsContract.Contacts._ID},null,null,null,
                    null);
            cursor.moveToFirst();

            if(cursor.getCount() > 0)
                do
                {
                    cursorNum = null;
                    String number = " No number available ";
                    if(!(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)).endsWith("0"))){
                    cursorNum = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER}, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "+ cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)),null,null,null);
                        int i = cursorNum.getCount();
                        cursorNum.moveToFirst();
                        number = cursorNum.getString(cursorNum.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }//if

                    contacts.add("Name: " + cursor.getString(0) + " \n \n Number: " + number);
                    cursorNum.close();
                }
                while (cursor.moveToNext());

            //Create intent to view contacts
            Intent intent = new Intent(getApplicationContext(),ActivityViewDataList.class);
            intent.putExtra("reqCode", PHONEBOOK_VIEW);
            startActivity(intent);

            cursor.close();



        });

        btn3.setOnClickListener(e->{
            Cursor cursor;
            Uri dirSmsUri;

            //Request permission
            ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.READ_SMS},0);

            dirSmsUri = Telephony.Sms.Inbox.CONTENT_URI;

            cursor = getContentResolver().query(dirSmsUri, new String[] {Telephony.Sms.Inbox.BODY},
                    null,null,Telephony.Sms.Inbox.DEFAULT_SORT_ORDER);

            cursor.moveToFirst();

            if(cursor.getCount() > 0)
            {
                do
                {
                    sms_s.add(cursor.getString(0));

                }
                while(cursor.moveToNext());
            }

            //Create intent to view SMSs
            Intent intent = new Intent(getApplicationContext(),ActivityViewDataList.class);
            intent.putExtra("reqCode", SMS_VIEW);
            startActivity(intent);

            cursor.close();
        });

        //button get all images
        btn4.setOnClickListener(e->{
            ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},0);

            imageDirUri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;

            imgCursor = getContentResolver().query(imageDirUri,null,null,null,null);


            //Create intent to view Images
            Intent intent = new Intent(getApplicationContext(),ActivityViewDataList.class);
            intent.putExtra("reqCode", IMAGES_VIEW);
            startActivity(intent);

        });

        btn5.setOnClickListener(e->{
            Toast.makeText(getApplicationContext(),"Still In Testing!",Toast.LENGTH_LONG).show();
        });

        btn6.setOnClickListener(e->{
            ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.INTERNET},811);
            Intent i = new Intent(this,NetworkCommunication.class);
            startActivity(i);

        });

        btn7.setOnClickListener(e->{
            ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.INTERNET},811);
            Intent i = new Intent(this,Server.class);
            startActivity(i);

        });

        btn8.setOnClickListener(e->{

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
