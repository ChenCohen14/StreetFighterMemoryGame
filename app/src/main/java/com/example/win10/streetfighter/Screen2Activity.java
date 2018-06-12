package com.example.win10.streetfighter;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Screen2Activity extends AppCompatActivity {
    private Intent intent;
    private final static int EASY = 2;
    private final static int MEDIUM = 4;
    private final static int ROWHARD = 4;
    private final static int COLHARD = 5;
    private final static int HARDMILLI = 60000;
    private final static int MEDIUMMILLI = 45000;
    private final static int EASYMILLI = 30000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_screen2);

        Bundle userData = getIntent().getExtras();
        if (userData == null)
            return;
        final String name = userData.getString("nameMessage");
        final String age = userData.getString("ageMessage");

        final TextView nameText = findViewById(R.id.nameTextView);

        nameText.setText(name + " " + age);

        Button btEasy = findViewById(R.id.easyButton);
        Button btMedium = findViewById(R.id.mediumButton);
        Button btHard = findViewById(R.id.hardButton);
        Button btRecord = findViewById(R.id.recordButton);


        btEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Screen2Activity.this, GameBoardActivity.class);
                intent.putExtra("nameMessage", name);
                intent.putExtra("row", EASY);
                intent.putExtra("col", EASY);
                intent.putExtra("milliseconds", EASYMILLI);
                startActivity(intent);
            }
        });

        btMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Screen2Activity.this, GameBoardActivity.class);
                intent.putExtra("nameMessage", name);
                intent.putExtra("row", MEDIUM);
                intent.putExtra("col", MEDIUM);
                intent.putExtra("milliseconds", MEDIUMMILLI);
                startActivity(intent);
            }
        });

        btHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Screen2Activity.this, GameBoardActivity.class);
                intent.putExtra("nameMessage", name);
                intent.putExtra("row", ROWHARD);
                intent.putExtra("col", COLHARD);
                intent.putExtra("milliseconds", HARDMILLI);
                startActivity(intent);
            }
        });

        btRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Screen2Activity.this,HighScores.class);
                startActivity(intent);


            }
        });



    }
}
