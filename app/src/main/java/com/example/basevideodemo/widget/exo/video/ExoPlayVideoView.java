package com.example.basevideodemo.widget.exo.video;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.basevideodemo.R;
import com.google.android.exoplayer2.ui.PlayerView;

/**
 * @author puyantao
 * @describe
 * @create 2020/9/10 16:32
 */
public class ExoPlayVideoView extends PlayerView implements View.OnClickListener {
    private Context mContext;
    private static final String SP_NAME = "ExoPlayVideoView";
    public static boolean WIFI_TIP_DIALOG_SHOWED = false;
    private boolean isCompletePlay = false;
    private VideoPlayUtils mVideoPlayUtils;
    private ExoVideoBean mExoVideoBean;
    private ImageView mExoBgIv;
    private ImageView mExoVideoFullscreen;
    private RelativeLayout mExoControllerTop;
    private View mExoBack;
    private TextView mExoVideoTitle;
    private ImageView mExoBatteryLevel;
    private TextView mExoVideoCurrentTime;
    private Boolean isFullScreen = false;

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
        intiEvent();
    }


    private void initView() {
        mExoBgIv = findViewById(R.id.video_iv);
        mExoVideoFullscreen = findViewById(R.id.exo_video_fullscreen);
        mExoControllerTop = findViewById(R.id.exo_controller_top);
        mExoBack = findViewById(R.id.exo_back);
        mExoVideoTitle = findViewById(R.id.exo_video_title);
        //点量
        mExoBatteryLevel = findViewById(R.id.exo_battery_level);
        //当前时间
        mExoVideoCurrentTime = findViewById(R.id.exo_video_current_time);
    }

    private void intiEvent() {
        mExoBack.setOnClickListener(this);
        mExoVideoFullscreen.setOnClickListener(this);
        mVideoPlayUtils.setOnVideoPlayListener(new VideoPlayUtils.OnVideoPlayListener() {
            @Override
            public void isStartPlay(ExoVideoBean bean) {
                isCompletePlay = false;
                if (!isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
                    showWifiDialog();
                    return;
                }
                setAllControlsVisible(View.GONE, View.GONE);
            }

            @Override
            public void isPausePlay(ExoVideoBean bean) {
            }

            @Override
            public void isEndPlay(ExoVideoBean bean) {
                isCompletePlay = true;
            }
        });
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
        mExoVideoTitle.setText(bean.getTitle());
        Glide.with(mContext).load(bean.getVideoPic()).into(mExoBgIv);
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        getPlayer().seekTo(sp.getLong(bean.getVideoUrl(), 0));
        setAllControlsVisible(View.VISIBLE, View.GONE);

    }


    /**
     * 设置试图隐藏和显示
     *
     * @param bg   背景图片
     * @param back 返回按鍵
     */
    public void setAllControlsVisible(int bg, int back) {
        mExoBgIv.setVisibility(bg);
        mExoBack.setVisibility(back);

    }

    /**
     * 销毁
     */
    public void onDestroy() {
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (getPlayer().getDuration() == getPlayer().getCurrentPosition() || isCompletePlay) {
            //播放完毕
            editor.putLong(mExoVideoBean.getVideoUrl(), 0);
        } else {
            editor.putLong(mExoVideoBean.getVideoUrl(), getPlayer().getCurrentPosition());
        }
        editor.commit();
        getPlayer().setPlayWhenReady(false);
        mVideoPlayUtils.releasePlayer();
    }


    public void stopPlay() {
        getPlayer().setPlayWhenReady(false);
    }

    public void startPlay() {
        getPlayer().setPlayWhenReady(true);
    }

    public boolean isCompletePlay() {
        return isCompletePlay;
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
            startPlay();
            WIFI_TIP_DIALOG_SHOWED = true;
        });
        builder.setNegativeButton("停止播放", (dialog, which) -> {
            dialog.dismiss();
            stopPlay();
        });
        builder.setOnCancelListener(DialogInterface::dismiss);
        builder.create().show();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.exo_video_fullscreen) {
            int flagsFullScreen = WindowManager.LayoutParams.FLAG_FULLSCREEN;
            if (isFullScreen) {
                mExoVideoFullscreen.setImageResource(R.drawable.video_enlarge);
                //退出全屏
                WindowManager.LayoutParams attrs = ((Activity) mContext).getWindow().getAttributes();
                attrs.flags &= (~flagsFullScreen);
                ((Activity) mContext).getWindow().setAttributes(attrs);
                ((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                isFullScreen = false;
            } else {
                // 设置全屏
                mExoVideoFullscreen.setImageResource(R.drawable.video_shrink);
                ((Activity) mContext).getWindow().addFlags(flagsFullScreen);
                isFullScreen = true;
            }
        } else if (id == R.id.exo_back) {
            //返回按键

        }
    }


}


































