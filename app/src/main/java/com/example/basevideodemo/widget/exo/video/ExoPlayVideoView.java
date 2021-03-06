package com.example.basevideodemo.widget.exo.video;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.basevideodemo.R;
import com.example.basevideodemo.widget.exo.ExoUtils;
import com.google.android.exoplayer2.ui.PlayerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    //坚屏
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
    private boolean isCompletePlay = false;
    private VideoPlayUtils mVideoPlayUtils;
    private ExoVideoBean mExoVideoBean;
    private ImageView mExoVideoFullscreen;
    private RelativeLayout mExoControllerTop;
    private View mExoBack;
    private TextView mExoVideoTitle;
    private ImageView mExoBatteryLevel;
    private TextView mExoVideoCurrentTime;
    private ImageView mExoStartBgIv;
    private ImageView mExoRetryStart;
    private TextView mExoReplayText;
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
        mExoVideoFullscreen = findViewById(R.id.exo_video_fullscreen);
        mExoControllerTop = findViewById(R.id.exo_controller_top);
        mExoBack = findViewById(R.id.exo_back);
        mExoVideoTitle = findViewById(R.id.exo_video_title);
        //背景图片
        mExoStartBgIv = findViewById(R.id.exo_start_bg_iv);
        //点量
        mExoBatteryLevel = findViewById(R.id.exo_battery_level);
        //当前时间
        mExoVideoCurrentTime = findViewById(R.id.exo_video_current_time);
        //是否重置
        mExoRetryStart = findViewById(R.id.exo_retry_start);
        mExoRetryStart.setOnClickListener(this);
        //播放完毕提醒
        mExoReplayText = findViewById(R.id.exo_replay_text);
    }

    private void intiEvent() {
        mExoBack.setOnClickListener(this);
        mExoStartBgIv.setOnClickListener(this);
        mExoVideoFullscreen.setOnClickListener(this);
        mVideoPlayUtils.setOnVideoPlayListener(new VideoPlayUtils.OnVideoPlayListener() {
            @Override
            public void isStartPlay(ExoVideoBean bean) {
                mExoRetryStart.setVisibility(GONE);
                mExoReplayText.setVisibility(GONE);
                mExoStartBgIv.setVisibility(GONE);
                isCompletePlay = false;
                setScreen(screen);
            }

            @Override
            public void isPausePlay(ExoVideoBean bean) {

            }

            @Override
            public void isEndPlay(ExoVideoBean bean) {
                isCompletePlay = true;
                mExoRetryStart.setImageResource(R.drawable.exo_click_replay_selector);
                mExoRetryStart.setVisibility(VISIBLE);
                mExoReplayText.setVisibility(VISIBLE);
                setAllControlsVisible(View.GONE);
            }
        });
    }


    /**
     * 添加数据
     *
     * @param bean
     */
    public void setUp(ExoVideoBean bean) {
        setUp(bean, SCREEN_TINY);
    }

    public void setUp(ExoVideoBean bean, int screen) {
        this.screen = screen;
        this.mExoVideoBean = bean;
        mVideoPlayUtils.play(this, bean);
        mExoVideoTitle.setText(bean.getTitle());
        mExoRetryStart.setVisibility(VISIBLE);
        mExoReplayText.setVisibility(GONE);
        mExoStartBgIv.setVisibility(VISIBLE);
        setAllControlsVisible( View.GONE);
        Glide.with(this).load(bean.getVideoPic()).into(mExoStartBgIv);
//        new MyVideoAsyncTask().execute(bean.getVideoUrl());
        mExoRetryStart.setImageResource(R.drawable.exo_click_play_selector);

        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        getPlayer().seekTo(sp.getLong(bean.getVideoUrl(), 0));
        setScreen(screen);
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
                setScreen(screen);
            }

        } else if (id == R.id.exo_back) {
            //返回按键
            if (screen == SCREEN_FULLSCREEN) {
                //quit fullscreen
                backPress();
            }
        } else if (id == R.id.exo_start_bg_iv) {
            //点击背景播放
            mExoRetryStart.setVisibility(GONE);
            mExoReplayText.setVisibility(GONE);
            setAllControlsVisible(View.GONE);
            startPlay();

        } else if (id == R.id.exo_retry_start) {
            //重播
            mExoRetryStart.setVisibility(GONE);
            mExoReplayText.setVisibility(GONE);
            mExoStartBgIv.setVisibility(GONE);
            setAllControlsVisible(View.GONE);
            startPlay();

        }
    }

    public void setScreen(int screen) {
        //特殊的个别的进入全屏的按钮在这里设置  只有setup的时候能用上
        switch (screen) {
            case SCREEN_NORMAL:
                setAllControlsVisible(View.GONE);
                break;
            case SCREEN_FULLSCREEN:
                setAllControlsVisible(View.VISIBLE);
                break;
            case SCREEN_TINY:
                break;
        }
    }


    /**
     * 退出
     *
     * @return
     */
    public boolean backPress() {
        if (CONTAINER_LIST.size() != 0) {
            gotoScreenNormal();
            setScreen(screen);
            return true;
        } else if (CONTAINER_LIST.size() == 0 && screen != SCREEN_NORMAL) {
            clearFloatScreen();
            setScreen(screen);
            return true;
        }
        return false;
    }


    /**
     * 退出全屏
     */
    private void gotoScreenNormal() {
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

    /**
     * 坚屏
     */
    private void clearFloatScreen() {
        ExoUtils.showStatusBar(getContext());
        ExoUtils.setRequestedOrientation(getContext(), NORMAL_ORIENTATION);
        ExoUtils.showSystemUI(getContext());

        setScreenTiny();
        ViewGroup vg = (ViewGroup) (ExoUtils.scanForActivity(getContext())).getWindow().getDecorView();
        vg.removeView(this);
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

    /**
     * 设置试图隐藏和显示
     *
     * @param back 返回按鍵
     */
    public void setAllControlsVisible(int back) {
        mExoBack.setVisibility(back);

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

    public void stopPlay() {
        findViewById(R.id.exo_pause).performClick();
    }

    public void startPlay() {
        findViewById(R.id.exo_play).performClick();
    }

    public boolean isCompletePlay() {
        return isCompletePlay;
    }

    /**
     * 播放视频第一帧图片的异步任务
     */
    class MyVideoAsyncTask extends AsyncTask<String, Void, Bitmap> {
        public MyVideoAsyncTask() {
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            return ExoUtils.getVideoFirstDosePic(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mExoStartBgIv.setImageBitmap(bitmap);
        }
    }

}


































