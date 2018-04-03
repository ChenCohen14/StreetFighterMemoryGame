package com.example.win10.streetfighter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Screen2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen2);

        Bundle userData = getIntent().getExtras();
        if(userData==null)
            return;
        String name = userData.getString("nameMessage");
        String age = userData.getString("ageMessage");

        final TextView nameText = findViewById(R.id.nameTextView);

        nameText.setText(name + " " + age);
    }
}
