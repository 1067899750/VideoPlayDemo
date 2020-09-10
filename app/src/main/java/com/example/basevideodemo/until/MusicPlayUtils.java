package com.example.basevideodemo.until;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.basevideodemo.model.BasePlayMusicBean;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * @author puyantao
 * @describe
 * @create 2020/9/10 9:28
 */
public class MusicPlayUtils {
    private static final String TAG = MusicPlayUtils.class.getSimpleName();
    private static MusicPlayUtils ourInstance;
    private ExoPlayer player;
    private BasePlayMusicBean bean;
    private DefaultTrackSelector trackSelector;
    private VideoPlayListener videoPlayListener;
    private WeakReference<Context> mContextWeakReference;

    public static MusicPlayUtils getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new MusicPlayUtils(context);
        }
        return ourInstance;
    }

    private MusicPlayUtils(Context context) {
        mContextWeakReference = new WeakReference<Context>(context);
        initPlay();
    }

    public BasePlayMusicBean getBean() {
        return bean;
    }

    public void initPlay() {
        if (player == null) {
            //传入工厂对象，以便创建选择磁道对象
            trackSelector = new DefaultTrackSelector();
            //根据选择磁道创建播放器对象
            player = ExoPlayerFactory.newSimpleInstance(mContextWeakReference.get(), trackSelector);
            player.setPlayWhenReady(false);
            player.addListener(new Player.EventListener() {

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    switch (playbackState) {
                        case Player.STATE_ENDED:
                            Log.d(TAG, "onPlayerStateChanged: + player.getCurrentTag()");
                            if (videoPlayListener != null) {
                                videoPlayListener.nowFinishPlay(bean);
                            }
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    public ExoPlayer getPlayer() {
        return player;
    }

    /**
     * 调用暂停
     */
    public void stop() {
        if (player != null) {
            player.stop();
        }
    }

    /**
     * 释放当前播放的类
     */
    public void releasePlayer() {
        if (player != null) {
            trackSelector = null;
            ourInstance = null;
            //释放播放器对象
            player.release();
            player = null;
            bean = null;
        }
    }

    public void play(BasePlayMusicBean bean) {
        play(bean, true);
    }

    public void play(BasePlayMusicBean bean, Boolean playWhenReady) {
        if (bean == null) {
            return;
        }
        initPlay();
        this.bean = bean;
        DefaultBandwidthMeter mDefaultBandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory mediaDataSourceFactory = new DefaultDataSourceFactory(mContextWeakReference.get(),
                mDefaultBandwidthMeter, new DefaultHttpDataSourceFactory("exoplayer-codelab", null,
                15000, 15000, true));
        //创建Extractor工厂对象，用于提取多媒体文件
        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        //创建数据源
        MediaSource mediaSources =
                new ExtractorMediaSource.Factory(mediaDataSourceFactory).
                        setExtractorsFactory(extractorsFactory).
                        setTag(bean.getPlayUrl()).
                        createMediaSource(Uri.parse(bean.getPlayUrl()));
        //添加监听
        //添加数据源到播放器
        player.prepare(mediaSources);
        player.setPlayWhenReady(playWhenReady);
    }


    public MusicPlayUtils setListener(VideoPlayListener videoPlayListener) {
        this.videoPlayListener = videoPlayListener;
        return ourInstance;
    }


    public interface VideoPlayListener {
        void nowFinishPlay(BasePlayMusicBean bean);
    }


}

















