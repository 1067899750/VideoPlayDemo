package com.example.basevideodemo.widget.exo.music;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.basevideodemo.R;
import com.example.basevideodemo.widget.exo.ExoUtils;
import com.google.android.exoplayer2.ui.PlayerControlView;

import androidx.annotation.Nullable;

/**
 * @author puyantao
 * @describe
 * @create 2020/9/10 9:33
 */
public class ExoPlayerMusicView extends PlayerControlView {
    private static final String SP_NAME = "PlayerMusicControlView";
    private ExoMusicBean mBasePlayMusicBean;
    private MusicPlayUtils mMusicPlayUtils;
    public static boolean WIFI_TIP_DIALOG_SHOWED = false;
    private Context mContext;
    private ImageView mMusicIv;
    private TextView mMusicTitleTv;
    private TextView mMusicDate;
    private boolean isCompletePlay = false;
    private boolean isWifiState = true;

    public ExoPlayerMusicView(Context context) {
        this(context, null);
    }

    public ExoPlayerMusicView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExoPlayerMusicView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        mMusicPlayUtils = new MusicPlayUtils(mContext);
        //图片
        mMusicIv = findViewById(R.id.music_iv);
        //标题
        mMusicTitleTv = findViewById(R.id.music_title_tv);
        //日期
        mMusicDate = findViewById(R.id.music_date);
        findViewById(R.id.exo_play_video).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ExoUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED && isWifiState) {
                    isWifiState = false;
                    stopPlay();
                    showWifiDialog();
                    return;
                }
                startPlay();
            }
        });
    }


    public void initData(ExoMusicBean bean) {
        this.mBasePlayMusicBean = bean;
        mMusicPlayUtils.play(bean, false);
        setShowTimeoutMs(-1);
        setPlayer(mMusicPlayUtils.getPlayer());

        Glide.with(mContext).load(bean.getPlayPic()).into(mMusicIv);
        mMusicTitleTv.setText(bean.getContentTitle());
        mMusicDate.setText(bean.getPlayDate());
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        getPlayer().seekTo(sp.getLong(bean.getPlayUrl(), 0));
        mMusicPlayUtils.setListener(new MusicPlayUtils.VideoPlayListener() {
            @Override
            public void isStartPlay(ExoMusicBean bean) {
                isCompletePlay = false;
            }

            @Override
            public void isPausePlay(ExoMusicBean bean) {
                findViewById(R.id.exo_play_video).setVisibility(VISIBLE);
            }

            @Override
            public void isEndPlay(ExoMusicBean bean) {
                isCompletePlay = true;
            }
        });

    }


    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }


    public void onDestroy() {
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (getPlayer().getDuration() == getPlayer().getCurrentPosition() || isCompletePlay) {
            //播放完毕
            editor.putLong(mBasePlayMusicBean.getPlayUrl(), 0);
        } else {
            editor.putLong(mBasePlayMusicBean.getPlayUrl(), getPlayer().getCurrentPosition());
        }
        editor.commit();
        getPlayer().setPlayWhenReady(false);
        mMusicPlayUtils.releasePlayer();
    }

    public void stopPlay() {
        getPlayer().setPlayWhenReady(false);
    }

    public void startPlay() {
        findViewById(R.id.exo_play_video).setVisibility(GONE);
        findViewById(R.id.exo_play).performClick();
    }

    /**
     * 是否完成播放
     *
     * @return
     */
    public boolean isCompletePlay() {
        return isCompletePlay;
    }


    private void showWifiDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("您当前正在使用移动网络，继续播放将消耗流量");
        builder.setPositiveButton("继续播放", (dialog, which) -> {
            WIFI_TIP_DIALOG_SHOWED = true;
            isWifiState = true;
            startPlay();
            dialog.dismiss();
        });
        builder.setNegativeButton("停止播放", (dialog, which) -> {
            isWifiState = true;
            dialog.dismiss();
            stopPlay();
        });
        builder.setOnCancelListener(DialogInterface::dismiss);
        builder.create().show();
    }


}















