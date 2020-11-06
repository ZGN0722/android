package com.example.wordapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class FrameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);

        Bundle bundle = this.getIntent().getExtras();

        TextView frameWord = findViewById(R.id.frameWord);
        TextView frameMeaning = findViewById(R.id.frameMeaning);
        TextView frameSample = findViewById(R.id.frameSample);

        frameWord.setText(bundle.getString(Words.Word.COLUMN_NAME_WORD));
        frameMeaning.setText(bundle.getString(Words.Word.COLUMN_NAME_MEANING));
        frameSample.setText(bundle.getString(Words.Word.COLUMN_NAME_SAMPLE));
    }
}