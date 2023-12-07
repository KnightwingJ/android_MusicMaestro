package com.example.musicmaestro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class tuner_sounds extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    Button home_button;
    ListView strings;
    List<String> stringList = new ArrayList<>();
    int[] stringArray = {R.raw.low_e, R.raw.a, R.raw.d, R.raw.g, R.raw.b, R.raw.e};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuner_sounds);

        home_button = findViewById(R.id.sound_home);
        strings = findViewById(R.id.strings);

        stringList.add("E");
        stringList.add("A");
        stringList.add("D");
        stringList.add("G");
        stringList.add("B");
        stringList.add("High E");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, stringList);
        strings.setAdapter(adapter); // Set all the file in the list.

        strings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int selectedStringIndex = position; // Assuming position corresponds to the array index
                int selectedAudioResource = stringArray[selectedStringIndex];
                startMediaPlayer(selectedAudioResource);
            }
        });

        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                Intent intent = new Intent(tuner_sounds.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startMediaPlayer(int audioResource) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }

            mediaPlayer = MediaPlayer.create(this, audioResource);
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
