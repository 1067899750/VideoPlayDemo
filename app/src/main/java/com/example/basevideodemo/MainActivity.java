package com.example.basevideodemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.basevideodemo.activity.VideoAndMusicActivity;
/**
  *@description
  *@author puyantao
  *@date 2020/9/4 19:37
  */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoAndMusicActivity.startVideoAndMusicActivity(MainActivity.this);
            }
        });
    }
}















