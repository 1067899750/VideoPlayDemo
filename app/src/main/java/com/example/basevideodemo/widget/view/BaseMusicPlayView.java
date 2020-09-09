package com.example.basevideodemo.widget.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.basevideodemo.R;
import com.example.basevideodemo.model.BasePlayMusicBean;
import com.example.basevideodemo.until.SeekBarUtils;
import com.example.basevideodemo.until.VideoTimeUtils;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author puyantao
 * @describe 音频播放器
 * @create 2020/9/4 9:06
 */
public class BaseMusicPlayView extends FrameLayout {
    private static final String SP_NAME = "BaseMusicPlayView";
    private Context mContext;
    private View rootView;
    private MediaPlayer mMediaPlayer;
    private ImageView mMusicIv;
    private RelativeLayout mPlayMusicRl;
    private ImageView mControlMusicIv;
    private TextView mMusicTitleTv;
    private TextView mMusicPlayCount;
    private TextView mMusicDate;
    private SeekBar mSeekBar;
    private TextView mPlayStartTimeTv;
    private TextView mPlayEndTimeTv;
    private BasePlayMusicBean mBasePlayMusicBean;
    private boolean isPlaying = false;

    private ScheduledThreadPoolExecutor mThreadPoolExecutor;
    /**
     * 互斥变量，防止进度条和定时器冲突。
     */
    private boolean isSeekBarChange;

    public BaseMusicPlayView(@NonNull Context context) {
        this(context, null);
    }

    public BaseMusicPlayView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseMusicPlayView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView(context);
    }

    private void initView(Context context) {
        mMediaPlayer = new MediaPlayer();
        rootView = LayoutInflater.from(mContext).inflate(R.layout.music_play_layout, null);
        addView(rootView);
        //图片
        mMusicIv = rootView.findViewById(R.id.music_iv);
        mPlayMusicRl = rootView.findViewById(R.id.play_music_rl);
        //控制是否播放
        mControlMusicIv = rootView.findViewById(R.id.control_music_iv);
        //标题
        mMusicTitleTv = rootView.findViewById(R.id.music_title_tv);
        //播放量
        mMusicPlayCount = rootView.findViewById(R.id.music_play_count);
        //日期
        mMusicDate = rootView.findViewById(R.id.music_date);
        //进度条
        mSeekBar = rootView.findViewById(R.id.music_play_sb);
        mSeekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seek_bar_progress));
//        SeekBarUtils.setProgressDrawable(mSeekBar, R.drawable.seek_bar_progress);
        //进度时间
        mPlayStartTimeTv = rootView.findViewById(R.id.play_start_time_tv);
        //结束时间
        mPlayEndTimeTv = rootView.findViewById(R.id.play_end_time_tv);

        initEvent();

    }

    private void initEvent() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //获取当前播放的位置
                initMediaPlayer();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeekBarChange = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSeekBarChange = false;
                //在当前位置播放
                mMediaPlayer.seekTo(seekBar.getProgress());
                mPlayStartTimeTv.setText(VideoTimeUtils.getTime(mMediaPlayer.getCurrentPosition() / 1000));
            }
        });

        mPlayMusicRl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlayMusicState();
            }
        });

        //网络流媒体的缓冲监听
        mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {

            }
        });

        //网络流媒体播放结束监听
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mControlMusicIv.setImageResource(R.drawable.pause_music);
            }
        });
        // 设置错误信息监听
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
    }

    /**
     * 判断是否播放
     *
     * @return
     */
    public boolean isPlayMusic() {
        return isPlaying;
    }


    /**
     * 获取当前文件的位置
     *
     * @return
     */
    public int getCurrentPosition() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    /**
     * 指定播放的位置（以毫秒为单位的时间）
     *
     * @param position
     */
    public void seekTo(int position) {
        mMediaPlayer.seekTo(position);
    }

    /**
     * 设置音量
     *
     * @param leftVolume
     * @param rightVolume
     */
    public void setVolume(float leftVolume, float rightVolume) {
        mMediaPlayer.setVolume(leftVolume, rightVolume);
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }


    /**
     * 设置播放状态
     */
    private void setPlayMusicState() {
        if (!mMediaPlayer.isPlaying()) {
            mControlMusicIv.setImageResource(R.drawable.play_music);
            mMediaPlayer.start();
            if (mThreadPoolExecutor != null) {
                mThreadPoolExecutor.shutdownNow();
            }
            mThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
            mThreadPoolExecutor.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    if (!isSeekBarChange) {
                        mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
                    }
                    if (!mMediaPlayer.isPlaying()) {
                        mSeekBar.setProgress(mMediaPlayer.getDuration());
                        //播放结束重置数据
                        if (mThreadPoolExecutor != null) {
                            mThreadPoolExecutor.shutdownNow();
                        }
                        isPlaying = false;
                    }
                }
            }, 0, 500, TimeUnit.MILLISECONDS);
            isPlaying = true;
        } else {
            setMusicPause();
        }
    }

    /**
     * 暂停
     */
    public void setMusicPause() {
        mControlMusicIv.setImageResource(R.drawable.pause_music);
        if (mMediaPlayer != null && isPlaying) {
            mMediaPlayer.pause();
            isPlaying = false;
        }
        if (mThreadPoolExecutor != null) {
            mThreadPoolExecutor.shutdownNow();
        }
    }

    /**
     * 设置播放数据
     *
     * @param musicBean
     */
    public void setPlayDate(BasePlayMusicBean musicBean) {
        this.mBasePlayMusicBean = musicBean;
        initMusicPlayData(musicBean);
    }

    /**
     * 初始化数据
     */
    private void initMusicPlayData(BasePlayMusicBean bean) {
        try {
            Glide.with(this).load(bean.getPlayPic()).into(mMusicIv);
            if (bean.isLocal()) {
                AssetFileDescriptor file = getResources().openRawResourceFd(bean.getRow());
                mMediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(),
                        file.getLength());
            } else {
                mMediaPlayer.setDataSource(bean.getPlayUrl());
            }
            mMediaPlayer.prepare();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMusicTitleTv.setText(bean.getContentTitle());
            mMusicPlayCount.setText("播放量 " + bean.getPlayCount());
            mMusicDate.setText(bean.getPlayDate());
            mSeekBar.setMax(mMediaPlayer.getDuration());
            mSeekBar.setProgress(0);

            SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
            mMediaPlayer.seekTo(sp.getInt(mBasePlayMusicBean.getPlayUrl(), 0));

            int duration = mMediaPlayer.getDuration() / 1000;
            mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
            mPlayEndTimeTv.setText(VideoTimeUtils.getTime(duration));
            initMediaPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 跟踪播放时间
     */
    private void initMediaPlayer() {
        int position = mMediaPlayer.getCurrentPosition() / 1000;
        mPlayStartTimeTv.setText(VideoTimeUtils.getTime(position));
    }


    /**
     * 释放
     */
    public void onDestroy() {
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (mMediaPlayer.getDuration() == mMediaPlayer.getCurrentPosition()) {
            //播放完毕
            editor.putInt(mBasePlayMusicBean.getPlayUrl(), 0);
        } else {
            editor.putInt(mBasePlayMusicBean.getPlayUrl(), mMediaPlayer.getCurrentPosition());
        }
        editor.commit();
        if (mThreadPoolExecutor != null) {
            mThreadPoolExecutor.shutdownNow();
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            isPlaying = false;
        }
    }

}





























