package com.example.musicmaestro;

import androidx.annotation.NonNull;
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
import android.view.ContextMenu;
import android.view.MenuItem;
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
        registerForContextMenu(recordingListView);


        recordingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedRecording = (String) parent.getItemAtPosition(position);
                startMediaPlayer(selectedRecording);
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


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    // Add this method to your activity
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        String selectedRecording = (String) recordingListView.getItemAtPosition(position);

        if (item.getItemId()==R.id.menu_delete){
            deleteRecording(selectedRecording);
            return true;
        }
        else{
            return super.onContextItemSelected(item);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MIC_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start recording
            } else {
                Toast.makeText(this, "Permission denied. Recording cannot proceed.", Toast.LENGTH_SHORT).show();
            }
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
        this.mediaRecorder = new MediaRecorder();
        this.mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        this.mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        this.mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        MediaRecorder.getAudioSourceMax();
        this.mediaRecorder.setOutputFile(getRecordingFilePath());

        try
        {
            this.mediaRecorder.prepare();
            this.mediaRecorder.start();
        } catch (final IllegalStateException e)
        {
            e.printStackTrace();
        } catch (final IOException e)
        {
            e.printStackTrace();
        }
    }

    public void Stop(View v){
        if(mediaRecorder!=null){
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder=null;

            adapter.clear();
            adapter.addAll(getRecordingList());
            adapter.notifyDataSetChanged();

            Toast.makeText(this, "Recording has finished", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "No recording to stop", Toast.LENGTH_LONG).show();

        }

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

    @Override
    protected void onStop(){
        super.onStop();
        if(mediaPlayer !=null){
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }
    private String getRecordingFilePath(){
        ContextWrapper contextWrapper=new ContextWrapper(getApplicationContext());
        File directory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        String fileName = "Recording_" + System.currentTimeMillis()+".mp3";
        File file = new File(directory,fileName);
        return file.getPath();
    }

    private void startMediaPlayer(String fileName) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(new File(recordingDirectory,fileName).getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(this,"Recording being played",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteRecording(String fileName) {
        File fileToDelete = new File(recordingDirectory,fileName);
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
        else{
            Toast.makeText(this,"File not found",Toast.LENGTH_SHORT).show();
        }
    }

}