package com.example.puzzlegame;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class PuzzGame extends AppCompatActivity {

    private static   int screenWidth;
    private static int screenHeight;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        Intent intent = getIntent();
        String back = intent.getStringExtra("back");
        int level = Integer.valueOf(intent.getStringExtra("level"));
        setContentView(new MainView(this,back,level));

    }


    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }
}