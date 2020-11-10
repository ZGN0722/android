package com.example.firstapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AnothActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anoth);


        Intent intent = getIntent();
        String str1= intent.getStringExtra("name");
        String str2= intent.getStringExtra("password");
        Toast.makeText(AnothActivity.this, str1+" "+str2, Toast.LENGTH_LONG).show();

        Button but = findViewById(R.id.back);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent = new Intent(AnothActivity.this, MainActivity.class);
                intent.putExtra("name", "lisi");
                intent.putExtra("password", "4");
                startActivity(intent);
            }

        });
    }
}