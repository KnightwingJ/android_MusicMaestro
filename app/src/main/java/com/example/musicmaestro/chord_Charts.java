package com.example.musicmaestro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class chord_Charts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chord_charts);

        Button eButton=findViewById(R.id.eButton);
        Button aButton=findViewById(R.id.aButton);
        Button dButton=findViewById(R.id.dButton);
        Button cButton=findViewById(R.id.cButton);
        Button gButton=findViewById(R.id.gButton);
        Button fButton=findViewById(R.id.fButton);

        ImageView chord=findViewById(R.id.chord);

        int[] chordArray={R.drawable.echord,
                R.drawable.achord,
                R.drawable.dchord,
                R.drawable.cchord,
                R.drawable.gchord,
                R.drawable.fchord};

        eButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chord.setImageResource(chordArray[0]);

            }
        });
        aButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chord.setImageResource(chordArray[1]);

            }
        });
        dButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chord.setImageResource(chordArray[2]);

            }
        });
        gButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chord.setImageResource(chordArray[4]);

            }
        });
        fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chord.setImageResource(chordArray[5]);

            }
        });


        Button home_button=findViewById(R.id.home_chord);



        home_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(chord_Charts.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}