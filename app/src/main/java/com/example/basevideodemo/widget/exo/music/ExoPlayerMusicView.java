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
import com.example.basevideodemo.model.BasePlayMusicBean;
import com.example.basevideodemo.widget.exo.music.MusicPlayUtils;
import com.google.android.exoplayer2.ui.PlayerControlView;

import androidx.annotation.Nullable;

/**
 * @author puyantao
 * @describe
 * @create 2020/9/10 9:33
 */
public class ExoPlayerMusicView extends PlayerControlView {
    private static final String SP_NAME = "PlayerMusicControlView";
    private BasePlayMusicBean mBasePlayMusicBean;
    private MusicPlayUtils mMusicPlayUtils;
    public static boolean WIFI_TIP_DIALOG_SHOWED = false;
    private Context mContext;
    private ImageView mMusicIv;
    private TextView mMusicTitleTv;
    private TextView mMusicDate;
    private ImageView mPlayMusic;
    private boolean isPlaying;

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
                if (!isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
                    showWifiDialog();
                    return;
                }
                setStartOrPauseMusic();
            }
        });
    }


    public void initData(BasePlayMusicBean bean) {
        this.mBasePlayMusicBean = bean;
        mMusicPlayUtils.play(bean, false);
        setShowTimeoutMs(-1);
        setPlayer(mMusicPlayUtils.getPlayer());

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

    private void setStartOrPauseMusic() {
        if (isPlaying) {
            stopPlay();
        } else {
            startPlay();
        }
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
        mMusicPlayUtils.releasePlayer();
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


    private boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    private void showWifiDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("您当前正在使用移动网络，继续播放将消耗流量");
        builder.setPositiveButton("继续播放", (dialog, which) -> {
            dialog.dismiss();
            setStartOrPauseMusic();
            WIFI_TIP_DIALOG_SHOWED = true;
        });
        builder.setNegativeButton("停止播放", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.setOnCancelListener(DialogInterface::dismiss);
        builder.create().show();
    }


}















