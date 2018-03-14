package com.example.lloyd.practice;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;

public class Server extends AppCompatActivity {
    Button btnSend;
    static String publicIp = "";
    EditText edtResponse;
    TextView edtLocalAddr,edtPublicAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
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

        btnSend = (Button) findViewById(R.id.btnSend);
        edtResponse = (EditText) findViewById(R.id.edtResponse);

        btnSend.setOnClickListener(e -> {
            new NetworkCommunication().startServer(edtResponse.getText().toString());

        });

        InetAddress inetAddress = null;
        //Get server local adress
        edtLocalAddr = (TextView) findViewById(R.id.edtLocalAddr);
        edtPublicAddr = (TextView) findViewById(R.id.edtPublicAddr);

        try {
            inetAddress = InetAddress.getLocalHost();
            if(inetAddress != null)
            inetAddress.getHostAddress();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        //DISPLAY LOCAL ADDRESS
        if(inetAddress != null)
        edtLocalAddr.setText("LocalIP address: "+inetAddress.getHostAddress());


        //get public address
        try
        {
            URL url_name = new URL("http://bot.whatismyipaddress.com");

            BufferedReader sc =
                    new BufferedReader(new InputStreamReader(url_name.openStream()));

            // reads system IPAddress
            publicIp = sc.readLine().trim();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        edtPublicAddr.setText("Public IP adress: "+publicIp);

    }

}
