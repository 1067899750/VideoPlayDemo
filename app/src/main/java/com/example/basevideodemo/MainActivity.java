package com.example.basevideodemo;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.basevideodemo.activity.VideoAndMusicActivity;
import com.example.basevideodemo.until.CommonUtils;
import com.example.basevideodemo.widget.view.MyJzvdStd;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import cn.jzvd.Jzvd;

/**
 * @author puyantao
 * @description
 * @date 2020/9/4 19:37
 */
public class MainActivity extends AppCompatActivity {
    private MyJzvdStd myJzvdStd;
    private PlayerView mExoPlayerView;
    private SimpleExoPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myJzvdStd = findViewById(R.id.jz_video);
        mExoPlayerView = findViewById(R.id.exo_video);

        initExoData();
        initJzvdData();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.requestPermissions(this);
        }
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoAndMusicActivity.startVideoAndMusicActivity(MainActivity.this);
            }
        });
    }

    private void initExoData() {
        mPlayer = new SimpleExoPlayer.Builder(this)
                .setTrackSelector(new DefaultTrackSelector(this))
                .setLoadControl(new DefaultLoadControl())
                .build();
        mPlayer.setPlayWhenReady(false);
        mExoPlayerView.setPlayer(mPlayer);
        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("user-agent");
        Uri uri = Uri.parse("http://jzvd.nathen.cn/342a5f7ef6124a4a8faf00e738b8bee4/cf6d9db0bd4d41f59d09ea0a81e918fd-5287d2089db37e62345123a1be272f8b.mp4");
        ProgressiveMediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
        mPlayer.prepare(mediaSource);
    }

    private void initJzvdData() {
        myJzvdStd.setUp("http://jzvd.nathen.cn/342a5f7ef6124a4a8faf00e738b8bee4/cf6d9db0bd4d41f59d09ea0a81e918fd-5287d2089db37e62345123a1be272f8b.mp4"
                , "饺子快长大");
        Glide.with(this).load("http://jzvd-pic.nathen.cn/jzvd-pic/1bb2ebbe-140d-4e2e-abd2-9e7e564f71ac.png")
                .into(myJzvdStd.thumbImageView);

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












