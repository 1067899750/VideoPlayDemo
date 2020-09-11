package com.example.basevideodemo.widget.exo.video;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.basevideodemo.R;
import com.google.android.exoplayer2.ui.PlayerView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

/**
 * @author puyantao
 * @describe
 * @create 2020/9/10 16:32
 */
public class ExoPlayVideoView extends PlayerView implements View.OnClickListener {
    public static int FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
    public static int NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    public static LinkedList<ViewGroup> CONTAINER_LIST = new LinkedList<>();
    //这个应该重写一下，刷新列表，新增列表的刷新，不打断播放，应该是个flag
    protected long goBakFullscreenTime = 0;
    public static final int SCREEN_NORMAL = 0;
    public static final int SCREEN_FULLSCREEN = 1;
    public static final int SCREEN_TINY = 2;
    public static boolean TOOL_BAR_EXIST = true;
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
    public int screen = -1;

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
        this.screen = SCREEN_NORMAL;
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
            if (screen == SCREEN_FULLSCREEN) {
                //quit fullscreen
                backPress();
            } else {
                //into fullscreen
                gotoScreenFullscreen();
            }

        } else if (id == R.id.exo_back) {
            //返回按键
            if (screen == SCREEN_FULLSCREEN) {
                //quit fullscreen
                backPress();
            }
        }
    }

    public void setScreen(int screen) {
        //特殊的个别的进入全屏的按钮在这里设置  只有setup的时候能用上
        switch (screen) {
            case SCREEN_NORMAL:
                setScreenNormal();
                break;
            case SCREEN_FULLSCREEN:
                setScreenFullscreen();
                break;
            case SCREEN_TINY:
                setScreenTiny();
                break;
        }
    }


    /**
     * 退出
     *
     * @return
     */
    public boolean backPress() {
        gotoScreenNormal();
        return true;
    }

    /**
     * 退出全屏
     */
    private void gotoScreenNormal() {
        setAllControlsVisible(View.GONE, View.GONE);
        mExoVideoFullscreen.setImageResource(R.drawable.video_enlarge);
        goBakFullscreenTime = System.currentTimeMillis();
        ViewGroup vg = (ViewGroup) (ExoUtils.scanForActivity(getContext())).getWindow().getDecorView();
        vg.removeView(this);
        CONTAINER_LIST.getLast().removeAllViews();
        CONTAINER_LIST.getLast().addView(this, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        CONTAINER_LIST.pop();

        setScreenNormal();
        ExoUtils.showStatusBar(getContext());
        ExoUtils.setRequestedOrientation(getContext(), NORMAL_ORIENTATION);
        ExoUtils.showSystemUI(getContext());
    }

    /**
     * 設置全屏
     */
    private void gotoScreenFullscreen() {
        setAllControlsVisible(View.GONE, View.VISIBLE);
        mExoVideoFullscreen.setImageResource(R.drawable.video_shrink);
        ViewGroup vg = (ViewGroup) getParent();
        vg.removeView(this);
        cloneAExo(vg);
        CONTAINER_LIST.add(vg);
        vg = (ViewGroup) (ExoUtils.scanForActivity(getContext())).getWindow().getDecorView();
        vg.addView(this, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        setScreenFullscreen();
        ExoUtils.hideStatusBar(getContext());
        ExoUtils.setRequestedOrientation(getContext(), FULLSCREEN_ORIENTATION);
        ////华为手机和有虚拟键的手机全屏时可隐藏虚拟键 issue:1326
        ExoUtils.hideSystemUI(getContext());
    }


    public void cloneAExo(ViewGroup vg) {
        try {
            Constructor<ExoPlayVideoView> constructor = (Constructor<ExoPlayVideoView>) ExoPlayVideoView.this.getClass().getConstructor(Context.class);
            ExoPlayVideoView exo = constructor.newInstance(getContext());
            exo.setId(getId());
            vg.addView(exo);
//            exo.setUp(jzDataSource.cloneMe(), SCREEN_NORMAL, mediaInterfaceClass);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void setScreenNormal() {
        screen = SCREEN_NORMAL;
    }

    public void setScreenFullscreen() {
        screen = SCREEN_FULLSCREEN;
    }

    public void setScreenTiny() {
        screen = SCREEN_TINY;
    }

}


































