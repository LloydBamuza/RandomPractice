package com.example.lloyd.practice;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;

import static com.example.lloyd.practice.MainActivity.appContext;
import static com.example.lloyd.practice.Server.publicIp;

public class NetworkCommunication extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    Button btnConnect;
    String text = " Spy App!";
    EditText edtServerIp;
    EditText edtServerPort;
    EditText edtServerResponse;
    GoogleApiClient googleApiClient;
    private static final String TAG = "GOOGLE DRIVE CONNECTION";
    private Boolean fileOperation = false;
    static final int REQUEST_CODE_RESOLUTION = 55;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_communication);
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

        //Get view references
        btnConnect = (Button) findViewById(R.id.btnConnect);
        edtServerIp = (EditText) findViewById((R.id.edtServerIP));
        edtServerPort = (EditText) findViewById((R.id.edtServerPort));
        edtServerResponse = (EditText) findViewById((R.id.txtFromServer));
        int startTime = 0;

        btnConnect.setOnClickListener(e->{
            connect();
        });

        if(googleApiClient == null)
        {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        googleApiClient.connect();
    }

    @Override
    protected void onResume()
    {
        super.onResume();


    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */



    void connect() {
        Socket socket = new Socket();

        String TAG = "Server Client";
        //Thread thread = null;

            new Thread(new Runnable() {
                String message = "";

                @Override
            public void run() {
                    StringBuilder stringBuilder = new StringBuilder();
                long time = 0l, startTime = 0l;
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                InputStream inputStream;

                try {
                    startTime = Calendar.getInstance().getTimeInMillis();
                    Log.d(TAG,"Connecting...");
                    //create IP object
                    InetAddress inetAddress = InetAddress.getByName(edtServerIp.getText().toString());

                    //esteblish connection to server
                    socket.connect((new InetSocketAddress(inetAddress,Integer.parseInt(edtServerPort.getText().toString()))),5000);
                } catch (Exception e) {
                }

                time = Calendar.getInstance().getTimeInMillis() - startTime;
                Log.d(TAG,"Connection Established in: "+ time +  "ms!");

                try{
                    //get message received from server
                    inputStream = socket.getInputStream();

                    int numBytesRead = 0;
                    byte[] buffer = new byte[1024];

                    //read data


                    Log.d(TAG,"Reading data...");
                    while((numBytesRead = inputStream.read(buffer)) != -1)
                    {
                        byteArrayOutputStream.write(buffer,0,numBytesRead);
                        message = byteArrayOutputStream.toString();
                        NetworkCommunication.this.runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                edtServerResponse.setText(edtServerResponse.getText() + message);
                            }

                        });
                            }

                }

                catch (Exception e){
                    e.printStackTrace();

                }
                //edtServerResponse.setText(stringBuilder.toString());

                }




            }).start();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        Toast.makeText(this,"Connected!",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        Log.i(TAG,"Connection Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        Log.i(TAG,"Google Api Client Connection Failed");

        if(!connectionResult.hasResolution())
        {
            GoogleApiAvailability.getInstance().getErrorDialog(this,connectionResult.getErrorCode(),0).show();
            return;
        }

        try
        {
            connectionResult.startResolutionForResult(this,REQUEST_CODE_RESOLUTION);
        }
        catch (Exception e)
        {
            Log.e(TAG,"Exception while handling resolution");
        }
    }

    ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback = new ResultCallback<DriveApi.DriveContentsResult>() {
        @Override
        public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
            if (driveContentsResult.getStatus().isSuccess()) {
                if (fileOperation == true) {
                    CreateFileOnGoogleDrive(driveContentsResult);
                }
            }
        }
    };

    ResultCallback<DriveFolder.DriveFileResult> fileCallBack = new ResultCallback<DriveFolder.DriveFileResult>() {
        @Override
        public void onResult(@NonNull DriveFolder.DriveFileResult driveFileResult) {
            if(driveFileResult.getStatus().isSuccess())
            {
                Toast.makeText(appContext,"File Created Successfully",Toast.LENGTH_LONG).show();
            }
            return;
        }
    };

    public void CreateFileOnGoogleDrive( DriveApi.DriveContentsResult result)
    {
        final DriveContents driveContents = result.getDriveContents();

        new Thread() {

            @Override
            public void run()
            {
                OutputStream outputStream = driveContents.getOutputStream();
                Writer writer = new OutputStreamWriter(outputStream);
                try
                {
                    writer.write(text);
                    writer.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.e(TAG,"Error while creating/ writing file. \n "+ e.getMessage());
                }

                MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
                        .setTitle("Test File")
                        .setMimeType("text/plain")
                        .build();

                //Create file in root folder
                Drive.DriveApi.getRootFolder(googleApiClient)
                        .createFile(googleApiClient,metadataChangeSet,driveContents)
                        .setResultCallback(fileCallBack);
            }
        }.start();
    }

    void createDriveFile()
    {
        fileOperation = true;

        //create new contents resource
        Drive.DriveApi.newDriveContents(googleApiClient)
                .setResultCallback(driveContentsCallback);
    }

    void startServer(String msg) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                createDriveFile();


                   /* InetAddress i = InetAddress.getByName("165.255.173.84");
                    String TAG = "Server Client";
                    int ID = 0;
                    ServerSocket serverSocket = new ServerSocket(12345,50,i);
                    OutputStream outputStream;
                    Socket socket;

                    while (true) {
                        Log.d(TAG,"Waiting for connection");
                        socket = serverSocket.accept();
                        Log.d(TAG,"Connection Established");

                        outputStream = socket.getOutputStream();

                        Log.d(TAG,"Writting message...");
                        //write message
                        ++ID;
                        outputStream.write("This is a message from server. \n".getBytes());
                        outputStream.write((msg.getBytes()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    //Toast.makeText(appContext,"Server started!",Toast.LENGTH_SHORT).show();

                }
            }*/


            }
        }).start();

    }
}
