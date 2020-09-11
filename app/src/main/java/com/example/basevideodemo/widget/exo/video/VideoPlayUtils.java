package com.example.basevideodemo.widget.exo.video;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.example.basevideodemo.model.BasePlayMusicBean;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * @author puyantao
 * @describe
 * @create 2020/9/11 10:43
 */
public class VideoPlayUtils {
    private Context mContext;
    private SimpleExoPlayer mExoPlayer;
    private ExoVideoBean mVideoBean;

    public VideoPlayUtils(Context context) {
        mContext = context;
        initPlay();
    }


    private void initPlay() {
        if (mExoPlayer == null) {
            mExoPlayer = new SimpleExoPlayer.Builder(mContext)
                    .build();

        }
    }

    public void play(ExoVideoBean bean) {
        play(bean, true);
    }

    public void play(ExoVideoBean bean, boolean playWhenReady) {
        if (bean == null || bean.getVideoUrl() == null) {
            //播放地址无效
            Toast.makeText(mContext, "播放地址无效", Toast.LENGTH_SHORT).show();
            return;
        }
        this.mVideoBean = bean;
        initPlay();

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,
                Util.getUserAgent(mContext, "myExoPlayer"));

        MediaSource mediaSource = new ProgressiveMediaSource
                .Factory(dataSourceFactory)
                .setTag(bean.getVideoUrl())
                .createMediaSource(Uri.parse(bean.getVideoUrl()));
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(playWhenReady);
    }


    public SimpleExoPlayer getExoPlayer() {
        return mExoPlayer;
    }


    /**
     * 释放当前播放的类
     */
    public void releasePlayer() {
        if (mExoPlayer != null) {
            //释放播放器对象
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}













