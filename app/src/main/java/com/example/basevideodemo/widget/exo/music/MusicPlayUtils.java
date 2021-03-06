package com.example.basevideodemo.widget.exo.music;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.basevideodemo.model.BasePlayMusicBean;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

/**
 * @author puyantao
 * @describe
 * @create 2020/9/10 9:28
 */
public class MusicPlayUtils {
    private static final String TAG = "MusicPlayUtils";
    private Context mContext;
    private SimpleExoPlayer player;
    private ExoMusicBean bean;
    private DefaultTrackSelector trackSelector;
    private VideoPlayListener videoPlayListener;


    public MusicPlayUtils(Context context) {
        this.mContext = context;
        initPlay();
    }

    public void initPlay() {
        if (player == null) {
            //传入工厂对象，以便创建选择磁道对象
            trackSelector = new DefaultTrackSelector(mContext);
            //根据选择磁道创建播放器对象
            player = new SimpleExoPlayer.Builder(mContext)
                    .setTrackSelector(trackSelector)
                    .build();
            player.setPlayWhenReady(false);
            player.addListener(new Player.EventListener() {

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    switch (playbackState) {
                        case Player.STATE_ENDED:
                            //播放结束
                            Log.d(TAG, "onPlayerStateChanged: + player.getCurrentTag()");
                            if (videoPlayListener != null) {
                                videoPlayListener.isEndPlay(bean);
                            }
                            break;
                        case Player.STATE_READY:
                            // 播放器可以立即从当前位置开始播放。如果{@link#getPlayWhenReady（）}为true，否则暂停。
                            //当点击暂停或者播放时都会调用此方法
                            //当跳转进度时，进度加载完成后调用此方法
                            if (player.getPlayWhenReady()){
                                if (videoPlayListener != null) {
                                    videoPlayListener.isStartPlay(bean);
                                }
                            } else {
                                if (videoPlayListener != null) {
                                    videoPlayListener.isPausePlay(bean);
                                }
                            }
                            break;
                        case Player.STATE_BUFFERING:
                            ////播放器无法立即从当前位置开始播放。这种状态通常需要加载更多数据时发生。
                            break;
                        case Player.STATE_IDLE:
                            //播放器没有可播放的媒体。
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
            //释放播放器对象
            player.release();
            player = null;
        }
    }

    public void play(ExoMusicBean bean) {
        play(bean, true);
    }

    public void play(ExoMusicBean bean, Boolean playWhenReady) {
        if (bean == null || bean.getPlayUrl() == null) {
            //播放地址无效
            Toast.makeText(mContext, "播放地址无效", Toast.LENGTH_SHORT).show();
            return;
        }
        initPlay();
        this.bean = bean;
        DefaultBandwidthMeter mDefaultBandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory mediaDataSourceFactory = new DefaultDataSourceFactory(mContext,
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


    public void setListener(VideoPlayListener videoPlayListener) {
        this.videoPlayListener = videoPlayListener;
    }


    /**
     * 播放监听器
     */
    public interface VideoPlayListener {
        void isStartPlay(ExoMusicBean bean);
        void isPausePlay(ExoMusicBean bean);
        void isEndPlay(ExoMusicBean bean);
    }


}

















