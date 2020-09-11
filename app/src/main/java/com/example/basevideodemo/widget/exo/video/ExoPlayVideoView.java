package com.example.basevideodemo.widget.exo.video;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.basevideodemo.R;
import com.google.android.exoplayer2.ui.PlayerView;

/**
 * @author puyantao
 * @describe
 * @create 2020/9/10 16:32
 */
public class ExoPlayVideoView extends PlayerView {
    private static final String SP_NAME = "ExoPlayVideoView";
    public static boolean WIFI_TIP_DIALOG_SHOWED = false;
    private Context mContext;
    private VideoPlayUtils mVideoPlayUtils;
    private ExoVideoBean mExoVideoBean;
    private ImageView mExoBgIv;
    private boolean isPlaying;

    public ExoPlayVideoView(Context context) {
        this(context, null);
    }

    public ExoPlayVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExoPlayVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        mVideoPlayUtils = new VideoPlayUtils(mContext);
        initView();
    }

    private void initView() {
        mExoBgIv = findViewById(R.id.video_iv);

    }


    /**
     * 添加数据
     *
     * @param bean
     */
    public void addData(ExoVideoBean bean) {
        this.mExoVideoBean = bean;
        mVideoPlayUtils.play(bean, false);
        setControllerShowTimeoutMs(-1);
        setPlayer(mVideoPlayUtils.getExoPlayer());

        Glide.with(mContext).load(bean.getVideoPic()).into(mExoBgIv);
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        getPlayer().seekTo(sp.getLong(bean.getVideoUrl(), 0));
    }


    public void onDestroy() {
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (getPlayer().getDuration() == getPlayer().getCurrentPosition()) {
            //播放完毕
            editor.putLong(mExoVideoBean.getVideoUrl(), 0);
        } else {
            editor.putLong(mExoVideoBean.getVideoUrl(), getPlayer().getCurrentPosition());
        }
        editor.commit();
        getPlayer().setPlayWhenReady(false);
        mVideoPlayUtils.releasePlayer();
    }


    private void setStartOrPauseMusic() {
        if (isPlaying) {
            stopPlay();
        } else {
            startPlay();
        }
    }

    public void stopPlay() {
        isPlaying = false;
        getPlayer().setPlayWhenReady(false);
//        mPlayMusic.setImageResource(R.drawable.pause_music);
//        if (findViewById(R.id.exo_pause).getVisibility() == View.VISIBLE) {
//            findViewById(R.id.exo_pause).performClick();
//        }
    }

    public void startPlay() {
        isPlaying = true;
        getPlayer().setPlayWhenReady(true);
//        mPlayMusic.setImageResource(R.drawable.play_music);

//        if (findViewById(R.id.exo_play).getVisibility() == View.VISIBLE) {
//            findViewById(R.id.exo_play).performClick();
//        }
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


































