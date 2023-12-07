package com.example.musicmaestro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class record extends AppCompatActivity {

    private static int MIC_CODE=200;
    MediaRecorder mediaRecorder;

    MediaPlayer mediaPlayer;

    ListView recordingListView;
    ArrayAdapter<String> adapter;

    File recordingDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        recordingDirectory = getRecordingDirectory();

        Button home_Button=findViewById(R.id.record_home);
        recordingListView =findViewById(R.id.recordingListView);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,getRecordingList());
        recordingListView.setAdapter(adapter);

        recordingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedRecording = (String) parent.getItemAtPosition(position);
                startMediaPlayer(selectedRecording);
                //Toast.makeText(this, "Audio is now playing", Toast.LENGTH_LONG).show();
            }
        });

        recordingListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedRecording = (String) parent.getItemAtPosition(position);
                // Implement delete logic using selectedRecording path
                deleteRecording(selectedRecording);
                return true;
            }
        });
        home_Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(record.this, MainActivity.class);
                startActivity(intent);
            }
        });



        if(isMicPresent()){
            getMicPermissions();
        }
    }

    private List<String> getRecordingList() {
        List<String> recordingList = new ArrayList<>();
        File[] recordingFiles = recordingDirectory.listFiles();

        if(recordingFiles!=null){
            for(File file: recordingFiles){
                recordingList.add(file.getName());
            }
        }
        return recordingList;
    }

    private File getRecordingDirectory(){
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        return contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
    }

    public void Record(View v){
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(getRecordingFilePath());
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();

            adapter.clear();
            adapter.addAll(getRecordingList());
            adapter.notifyDataSetChanged();

            Toast.makeText(this, "Recording has begun", Toast.LENGTH_LONG).show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void Stop(View v){
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder=null;

        adapter.clear();
        adapter.addAll(getRecordingList());
        adapter.notifyDataSetChanged();

        Toast.makeText(this, "Recording has finished", Toast.LENGTH_LONG).show();
    }

    public void Home(View v)
    {
        Intent intent = new Intent(record.this, MainActivity.class);
        startActivity(intent);
    }

    private boolean isMicPresent(){
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)){
            return true;
        }
        else{
            return false;
        }
    }
    private void getMicPermissions (){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},MIC_CODE);
        }
    }

    private String getRecordingFilePath(){
        ContextWrapper contextWrapper=new ContextWrapper(getApplicationContext());
        File directory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        String fileName = "Recording_" + System.currentTimeMillis()+".mp3";
        File file = new File(directory,fileName);
        return file.getPath();
    }

    private void startMediaPlayer(String filePath) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteRecording(String filePath) {
        File fileToDelete = new File(filePath);
        if (fileToDelete.exists()) {
            if (fileToDelete.delete()) {
                Toast.makeText(this, "Recording deleted", Toast.LENGTH_SHORT).show();
                // Update the ListView
                adapter.clear();
                adapter.addAll(getRecordingList());
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Failed to delete recording", Toast.LENGTH_SHORT).show();
            }
        }
    }

}