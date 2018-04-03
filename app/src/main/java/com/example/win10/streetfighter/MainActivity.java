package com.example.win10.streetfighter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view){
        Intent intent = new Intent(this, Screen2Activity.class);
        final EditText nameText = (EditText) findViewById(R.id.nameEditText);
        final EditText ageText = (EditText) findViewById(R.id.ageEditText);

        String name = nameText.getText().toString();
        String age = ageText.getText().toString();

        intent.putExtra("nameMessage", name);
        intent.putExtra("ageMessage", age);


        startActivity(intent);
    }
}
