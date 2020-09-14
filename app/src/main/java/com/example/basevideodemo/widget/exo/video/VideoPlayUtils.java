package com.example.basevideodemo.widget.exo.video;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
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
    private OnVideoPlayListener mOnVideoPlayListener;

    public VideoPlayUtils(Context context) {
        mContext = context;
        initPlay();
    }


    private void initPlay() {
        if (mExoPlayer == null) {
            //传入工厂对象，以便创建选择磁道对象
            mExoPlayer = new SimpleExoPlayer.Builder(mContext)
                    .setTrackSelector(new DefaultTrackSelector(mContext))
                    .setLoadControl(new DefaultLoadControl())
                    .build();
            mExoPlayer.addListener(new Player.EventListener() {

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    switch (playbackState) {
                        case Player.STATE_ENDED:
                            //播放结束
                            if (mOnVideoPlayListener != null) {
                                mOnVideoPlayListener.isEndPlay(mVideoBean);
                            }
                            break;
                        case Player.STATE_READY:
                            // 播放器可以立即从当前位置开始播放。如果{@link#getPlayWhenReady（）}为true，否则暂停。
                            //当点击暂停或者播放时都会调用此方法
                            //当跳转进度时，进度加载完成后调用此方法
                            if (mExoPlayer.getPlayWhenReady()) {
                                if (mOnVideoPlayListener != null) {
                                    mOnVideoPlayListener.isStartPlay(mVideoBean);
                                }
                            } else {
                                if (mOnVideoPlayListener != null) {
                                    mOnVideoPlayListener.isPausePlay(mVideoBean);
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

    public void play(PlayerView playerView, ExoVideoBean bean) {
        play(playerView, bean, false);
    }

    public void play(PlayerView playerView, ExoVideoBean bean, boolean playWhenReady) {
        if (bean == null || bean.getVideoUrl() == null) {
            //播放地址无效
            Toast.makeText(mContext, "播放地址无效", Toast.LENGTH_SHORT).show();
            return;
        }
        this.mVideoBean = bean;
        initPlay();
        //
        DefaultHttpDataSourceFactory defaultHttpDataSourceFactory = new DefaultHttpDataSourceFactory("user-agent");
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,
                Util.getUserAgent(mContext, "myExoPlayer"));
        //媒体资源
        MediaSource mediaSource = new ProgressiveMediaSource
                .Factory(dataSourceFactory)
                .setTag(bean.getVideoUrl())
                .createMediaSource(Uri.parse(bean.getVideoUrl()));
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(playWhenReady);
        //准备播放
        playerView.setControllerShowTimeoutMs(-1);
        playerView.setPlayer(mExoPlayer);
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


    public void setOnVideoPlayListener(OnVideoPlayListener videoPlayListener) {
        this.mOnVideoPlayListener = videoPlayListener;
    }


    /**
     * 播放监听器
     */
    public interface OnVideoPlayListener {
        void isStartPlay(ExoVideoBean bean);

        void isPausePlay(ExoVideoBean bean);

        void isEndPlay(ExoVideoBean bean);
    }

}













