package com.example.musicmaestro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;

public class chord_Charts extends AppCompatActivity {
    private boolean maj_min;
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
        Switch major_minor=(Switch)findViewById(R.id.major_minor);

        maj_min = major_minor.isChecked();

        ImageView chord=findViewById(R.id.chord);

        major_minor.setTextOn("Minor");
        major_minor.setTextOff("Major");

        int[] chordArray={R.drawable.echord,
                R.drawable.achord,
                R.drawable.dchord,
                R.drawable.cchord,
                R.drawable.gchord,
                R.drawable.fchord};

        int[] minorArray={R.drawable.eminor,
                R.drawable.aminor,
                R.drawable.dminor,
                R.drawable.cminor,
                R.drawable.gminorpng,
                R.drawable.fminor};

        //Switch to change from Major chords to Minor Chords
        major_minor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maj_min =!maj_min;
                major_minor.setText("Minor");
            }
        });
        //Multiple buttons for each chord
        eButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!maj_min){
                    chord.setImageResource(chordArray[0]);
                }
                else{
                    chord.setImageResource(minorArray[0]);
                }

            }
        });
        aButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!maj_min){
                    chord.setImageResource(chordArray[1]);
                }
                else{
                    chord.setImageResource(minorArray[1]);
                }

            }
        });
        dButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!maj_min){
                    chord.setImageResource(chordArray[2]);
                }
                else{
                    chord.setImageResource(minorArray[2]);
                }

            }
        });
        gButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!maj_min){
                    chord.setImageResource(chordArray[4]);
                }
                else{
                    chord.setImageResource(minorArray[4]);
                }

            }
        });
        fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!maj_min){
                    chord.setImageResource(chordArray[5]);
                }
                else{
                    chord.setImageResource(minorArray[5]);
                }

            }
        });

        cButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!maj_min){
                    chord.setImageResource(chordArray[3]);
                }
                else{
                    chord.setImageResource(minorArray[3]);
                }

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