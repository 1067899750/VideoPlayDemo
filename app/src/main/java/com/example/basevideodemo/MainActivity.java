package com.example.basevideodemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.basevideodemo.activity.VideoAndMusicActivity;
import com.example.basevideodemo.until.CommonUtils;
import com.example.basevideodemo.widget.view.MyJzvdStd;

import cn.jzvd.Jzvd;

/**
  *@description
  *@author puyantao
  *@date 2020/9/4 19:37
  */
public class MainActivity extends AppCompatActivity {
    MyJzvdStd myJzvdStd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        myJzvdStd = findViewById(R.id.jz_video);
        myJzvdStd.setUp("http://jzvd.nathen.cn/342a5f7ef6124a4a8faf00e738b8bee4/cf6d9db0bd4d41f59d09ea0a81e918fd-5287d2089db37e62345123a1be272f8b.mp4"
                , "饺子快长大");
        Glide.with(this).load("http://jzvd-pic.nathen.cn/jzvd-pic/1bb2ebbe-140d-4e2e-abd2-9e7e564f71ac.png").into(myJzvdStd.thumbImageView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            CommonUtils.requestPermissions(this);
        }
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoAndMusicActivity.startVideoAndMusicActivity(MainActivity.this);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.resetAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }
}












