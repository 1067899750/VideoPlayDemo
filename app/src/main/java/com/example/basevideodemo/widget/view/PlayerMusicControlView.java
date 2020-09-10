package com.example.basevideodemo.widget.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.basevideodemo.R;
import com.example.basevideodemo.model.BasePlayMusicBean;
import com.example.basevideodemo.until.MusicPlayUtils;
import com.google.android.exoplayer2.ui.PlayerControlView;

import androidx.annotation.Nullable;

/**
 * @author puyantao
 * @describe
 * @create 2020/9/10 9:33
 */
public class PlayerMusicControlView extends PlayerControlView {
    private BasePlayMusicBean mBasePlayMusicBean;
    private static final String SP_NAME = "PlayerMusicControlView";
    private Context mContext;
    private ImageView mMusicIv;
    private TextView mMusicTitleTv;
    private TextView mMusicDate;
    private ImageView mPlayMusic;
    private boolean isPlaying;

    public PlayerMusicControlView(Context context) {
        this(context, null);
    }

    public PlayerMusicControlView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayerMusicControlView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        isPlaying = false;
        //图片
        mMusicIv = findViewById(R.id.music_iv);
        //标题
        mMusicTitleTv = findViewById(R.id.music_title_tv);
        //日期
        mMusicDate = findViewById(R.id.music_date);
        mPlayMusic = findViewById(R.id.play_music);
        mPlayMusic.setImageResource(R.drawable.pause_music);

        findViewById(R.id.control_play).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    stopPlay();
                } else {
                    startPlay();
                }
            }
        });
    }


    public void initData(BasePlayMusicBean bean) {
        this.mBasePlayMusicBean = bean;
        MusicPlayUtils.getInstance(mContext).play(bean, false);
        setShowTimeoutMs(-1);
        setPlayer(MusicPlayUtils.getInstance(mContext).getPlayer());

        Glide.with(mContext).load(bean.getPlayPic()).into(mMusicIv);
        mMusicTitleTv.setText(bean.getContentTitle());
        mMusicDate.setText(bean.getPlayDate());
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        getPlayer().seekTo(sp.getLong(bean.getPlayUrl(), 0));

    }


    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }


    public void onDestroy() {
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (getPlayer().getDuration() == getPlayer().getCurrentPosition()) {
            //播放完毕
            editor.putLong(mBasePlayMusicBean.getPlayUrl(), 0);
        } else {
            editor.putLong(mBasePlayMusicBean.getPlayUrl(), getPlayer().getCurrentPosition());
        }
        editor.commit();
        getPlayer().setPlayWhenReady(false);
        MusicPlayUtils.getInstance(mContext).releasePlayer();
    }

    public void stopPlay() {
        isPlaying = false;
        getPlayer().setPlayWhenReady(false);
        mPlayMusic.setImageResource(R.drawable.pause_music);
//        if (findViewById(R.id.exo_pause).getVisibility() == View.VISIBLE) {
//            findViewById(R.id.exo_pause).performClick();
//        }
    }

    public void startPlay() {
        isPlaying = true;
        getPlayer().setPlayWhenReady(true);
        mPlayMusic.setImageResource(R.drawable.play_music);

//        if (findViewById(R.id.exo_play).getVisibility() == View.VISIBLE) {
//            findViewById(R.id.exo_play).performClick();
//        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}















