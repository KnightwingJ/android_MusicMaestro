package com.example.musicmaestro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class record extends AppCompatActivity {

    private static final int AUDIO_PERMISSION_REQUEST_CODE = 1;

    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String audioFilePath;
    private ListView recordedAudioListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> recordedAudioList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        recordedAudioListView = findViewById(R.id.recordedAudioListView);

        recordedAudioList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recordedAudioList);
        recordedAudioListView.setAdapter(adapter);

        checkPermission();

        recordedAudioListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedAudioFile = recordedAudioList.get(position);
            playAudio(selectedAudioFile);
        });

        recordedAudioListView.setOnItemLongClickListener((parent, view, position, id) -> {
            String selectedAudioFile = recordedAudioList.get(position);
            deleteAudio(selectedAudioFile, position);
            return true;
        });

        Button recordButton = findViewById(R.id.recordButton);
        Button stopButton = findViewById(R.id.stopButton);

        recordButton.setOnClickListener(v -> startRecording());

        stopButton.setOnClickListener(v -> stopRecording());

        Button home_button=findViewById(R.id.home_record);

        home_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(record.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    AUDIO_PERMISSION_REQUEST_CODE);
        } else {
            initializeAudioRecorder();
            loadRecordedAudioList();
        }
    }

    private void initializeAudioRecorder() {
        audioFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/recording.3gp";

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(audioFilePath);
    }

    private void startRecording() {
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            Toast.makeText(record.this, "Recording started", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.release();
        Toast.makeText(record.this, "Recording stopped", Toast.LENGTH_SHORT).show();
        loadRecordedAudioList();
        initializeAudioRecorder();
    }

    private void playAudio(String audioFile) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioFile);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(this, "Playing audio", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteAudio(String audioFile, int position) {
        File file = new File(audioFile);
        if (file.delete()) {
            recordedAudioList.remove(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Audio deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to delete audio", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadRecordedAudioList() {
        recordedAudioList.clear();

        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath());

        File[] files = directory.listFiles((dir, name) -> name.endsWith(".3gp"));

        if (files != null) {
            for (File file : files) {
                recordedAudioList.add(file.getAbsolutePath());
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaRecorder != null) {
            mediaRecorder.release();
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == AUDIO_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeAudioRecorder();
                loadRecordedAudioList();
            } else {
                // Handle permission denial
                Toast.makeText(this, "Permission denied. Cannot record audio.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}