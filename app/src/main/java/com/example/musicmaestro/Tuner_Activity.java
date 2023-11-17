package com.example.musicmaestro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Tuner_Activity extends AppCompatActivity {

    private static final int AUDIO_PERMISSION_REQUEST_CODE = 1;

    private AudioRecord audioRecord;
    private boolean isListening = false;
    private TextView statusTextView;

    TextView noteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuner);

        statusTextView = findViewById(R.id.statusTextView);

        noteView=findViewById(R.id.note);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    AUDIO_PERMISSION_REQUEST_CODE);
        } else {
            startListening();
        }

        Button tuner_button = findViewById(R.id.home_button);

        tuner_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Tuner_Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startListening() {
        int minBufferSize = AudioRecord.getMinBufferSize(44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                minBufferSize);

        audioRecord.startRecording();

        isListening = true;

        new Thread(() -> {
            while (isListening) {
                short[] buffer = new short[minBufferSize];
                audioRecord.read(buffer, 0, minBufferSize);
                double frequency = calculateFrequency(buffer, 44100);

                // Implement your logic to determine whether the string is tight or loose
                // For simplicity, let's assume a reference frequency and check against it
                double referenceFrequency = 440.0;
                displayTuningStatus(frequency, referenceFrequency);
            }
        }).start();
    }

    private double calculateFrequency(short[] audioBuffer, int sampleRate) {
        // Implement your frequency calculation logic here
        // You may use algorithms like FFT to calculate the dominant frequency
        // For simplicity, a placeholder value is returned
        int tauEstimate = Yin.pitchDetection(audioBuffer, sampleRate);
        double frequency = (double)sampleRate/tauEstimate;
        return frequency;
    }

    private void displayTuningStatus(double currentFrequency, double referenceFrequency) {
        // Implement your logic to display tuning status
        // For simplicity, a placeholder text is set to the TextView
        String status = (currentFrequency > referenceFrequency) ? "Loosen" : "Tighten";

        new Handler(Looper.getMainLooper()).post(() ->
                statusTextView.setText("Status: " + status));

        new Handler(Looper.getMainLooper()).post(() ->
                noteView.setText("Note: "+ currentFrequency));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopListening();
    }

    private void stopListening() {
        isListening = false;
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == AUDIO_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startListening();
            } else {
                // Handle permission denial
            }
        }
    }
}