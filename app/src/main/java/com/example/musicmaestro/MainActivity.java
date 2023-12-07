package com.example.musicmaestro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button tuner_button = findViewById(R.id.tuner_button);
        Button chord_button = findViewById(R.id.chord_button);
        Button record_button = findViewById(R.id.record_button);

        tuner_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, tuner_sounds.class);
                startActivity(intent);
            }
        });

        chord_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, chord_Charts.class);
                startActivity(intent);
            }
        });

        record_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, record.class);
                startActivity(intent);
            }
        });

    }
}