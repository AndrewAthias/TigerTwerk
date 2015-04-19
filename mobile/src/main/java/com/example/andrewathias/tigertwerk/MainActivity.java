package com.example.andrewathias.tigertwerk;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.File;
import java.util.ArrayList;
import org.w3c.dom.Document;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MainActivity extends ActionBarActivity {
    private VideoView mVideoView;
    ListView lv;
    String address = "192.168.1.61";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.lvPlaylist);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_list_item_1);
        adapter.addAll(1,2,3,4,5,6);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new Thread(){
                    public void run(){

                        try{
                        URL url = new URL("http://" +address+ ":8090/key");
                        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                        connection.setDoOutput(true);
                        connection.getOutputStream().write(("<key state=\"press\" sender=\"Gabbo\">PRESET_" + (position+1) + "</key>").getBytes());
                        connection.getOutputStream().flush();
                        connection.getOutputStream().close();

                        Log.d("twerk","<key state=\"press\" sender=\"Gabbo\">PRESET_" + (position+1) + "</key>");
                        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                        Document document = builder.parse(connection.getInputStream());
                        connection.getInputStream().close();
                    } catch (Exception e){e.printStackTrace();};



                    try{
                        URL url = new URL("http://" +address+ ":8090/key");
                        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                        connection.setDoOutput(true);
                        connection.getOutputStream().write(("<key state=\"release\" sender=\"Gabbo\">PRESET_" + (position+1) + "</key>").getBytes());
                        connection.getOutputStream().flush();
                        connection.getOutputStream().close();

                        Log.d("twerk","<key state=\"release\" sender=\"Gabbo\">PRESET_" + (position+1) + "</key>");
                        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                        Document document = builder.parse(connection.getInputStream());
                        connection.getInputStream().close();
                    } catch (Exception e){e.printStackTrace();};

                }
                }.start();

                //new selectPresetsTasks().execute(position+1);
                Log.d("TigTwerk", "Preset" +(position+1));
            }
        });


        mVideoView = (VideoView) findViewById(R.id.videoView);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakeVideoIntent();

            }
        });

    }
    static final int REQUEST_VIDEO_CAPTURE = 1;

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
         {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();
            mVideoView.setVideoURI(videoUri);
            mVideoView.start();
        }


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
