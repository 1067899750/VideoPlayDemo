package com.example.basevideodemo.widget.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.basevideodemo.R;
import com.example.basevideodemo.model.BasePlayMusicBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import cn.jzvd.JZDataSource;
import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;

/**
 * @author puyantao
 * @describe
 * @create 2020/9/9 16:39
 */
public class PlatMusicStd extends Jzvd {
    public static int LAST_GET_BATTERYLEVEL_PERCENT = 70;
    protected static Timer DISMISS_CONTROL_VIEW_TIMER;
    private BasePlayMusicBean mBasePlayMusicBean;
    private ImageView mMusicIv;
    private TextView mMusicTitleTv;
    private TextView mMusicPlayCount;
    private TextView mMusicDate;
    public ProgressBar loadingProgressBar;

    public PopupWindow clarityPopWindow;
    protected DismissControlViewTimerTask mDismissControlViewTimerTask;
    protected Dialog mProgressDialog;
    protected Dialog mVolumeDialog;
    protected Dialog mBrightnessDialog;


    public PlatMusicStd(Context context) {
        super(context);
    }

    public PlatMusicStd(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.plat_music_layout_std;
    }


    @Override
    public void init(Context context) {
        super.init(context);
        loadingProgressBar = findViewById(R.id.loading);
        //图片
        mMusicIv = findViewById(R.id.music_iv);
        //标题
        mMusicTitleTv = findViewById(R.id.music_title_tv);
        //播放量
        mMusicPlayCount = findViewById(R.id.music_play_count);
        //日期
        mMusicDate = findViewById(R.id.music_date);
    }

    @Override
    public void setUp(JZDataSource jzDataSource, int screen, Class mediaInterfaceClass) {
        super.setUp(jzDataSource, screen, mediaInterfaceClass);
        setScreen(screen);
    }

    public void changeStartButtonSize(int size) {
        ViewGroup.LayoutParams lp = startButton.getLayoutParams();
        lp.height = size;
        lp.width = size;
        lp = loadingProgressBar.getLayoutParams();
        lp.height = size;
        lp.width = size;
    }


    @Override
    public void onStateNormal() {
        super.onStateNormal();
        changeUiToNormal();
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
        changeUiToPreparing();
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();

    }

    @Override
    public void onStatePause() {
        super.onStatePause();
        changeUiToPauseShow();
        cancelDismissControlViewTimer();
    }

    @Override
    public void onStateError() {
        super.onStateError();
        changeUiToError();
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        changeUiToComplete();
        cancelDismissControlViewTimer();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        if (id == R.id.bottom_seek_progress) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    cancelDismissControlViewTimer();
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    startDismissControlViewTimer();
                    break;
                }
                default:
                    break;
            }
        }
        return super.onTouch(v, event);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (jzDataSource == null || jzDataSource.urlsMap.isEmpty() || jzDataSource.getCurrentUrl() == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_url), Toast.LENGTH_SHORT).show();
            return;
        }
        if (i == R.id.start) {
            if (state == STATE_NORMAL) {
                if (!jzDataSource.getCurrentUrl().toString().startsWith("file") && !
                        jzDataSource.getCurrentUrl().toString().startsWith("/") &&
                        !JZUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {//这个可以放到std中
                    showWifiDialog();
                    return;
                }
                startVideo();
            } else if (state == STATE_PREPARING) {
                if (!jzDataSource.getCurrentUrl().toString().startsWith("file") && !
                        jzDataSource.getCurrentUrl().toString().startsWith("/") &&
                        !JZUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {//这个可以放到std中
                    showWifiDialog();
                    return;
                }
                mediaInterface.start();
                onStatePlaying();
            } else if (state == STATE_PAUSE) {
                mediaInterface.start();
                onStatePlaying();
            } else if (state == STATE_AUTO_COMPLETE) {
                onClickUiToggle();
                startVideo();
            }
        }
    }


    public void onClickUiToggle() {
        if (state == STATE_PREPARING) {
            changeUiToPreparing();
        } else if (state == STATE_PLAYING) {
            changeUiToPlayingShow();
        } else if (state == STATE_PAUSE) {
            changeUiToPauseShow();
        }
    }


    @Override
    public void setScreenNormal() {
        super.setScreenNormal();
        changeStartButtonSize((int) getResources().getDimension(R.dimen.start_button_w_h_normal));
    }


    @Override
    public void setScreenTiny() {
        super.setScreenTiny();
    }

    @Override
    public void showWifiDialog() {
        super.showWifiDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getResources().getString(R.string.tips_not_wifi));
        builder.setPositiveButton(getResources().getString(R.string.tips_not_wifi_confirm), (dialog, which) -> {
            dialog.dismiss();
            startVideo();
            WIFI_TIP_DIALOG_SHOWED = true;
        });
        builder.setNegativeButton(getResources().getString(R.string.tips_not_wifi_cancel), (dialog, which) -> {
            dialog.dismiss();
            clearFloatScreen();
        });
        builder.setOnCancelListener(DialogInterface::dismiss);
        builder.create().show();
    }

    /**
     * 因为框架中有 bug 所以重重写
     */
    @Override
    public void clearFloatScreen() {
        JZUtils.showStatusBar(getContext());
        JZUtils.setRequestedOrientation(getContext(), NORMAL_ORIENTATION);
        JZUtils.showSystemUI(getContext());

        ViewGroup vg = (ViewGroup) (JZUtils.scanForActivity(getContext())).getWindow().getDecorView();
        vg.removeView(this);
        if (mediaInterface != null) {
            mediaInterface.release();
        }
        CURRENT_JZVD = null;
    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        super.onStartTrackingTouch(seekBar);
        cancelDismissControlViewTimer();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        super.onStopTrackingTouch(seekBar);
        startDismissControlViewTimer();
    }


    @Override
    public void onProgress(int progress, long position, long duration) {
        super.onProgress(progress, position, duration);
        if (progress != 0) {
        }
    }

    @Override
    public void setBufferProgress(int bufferProgress) {
        super.setBufferProgress(bufferProgress);
        if (bufferProgress != 0) {

        }
    }

    @Override
    public void resetProgressAndTime() {
        super.resetProgressAndTime();
    }

    public void changeUiToNormal() {
        switch (screen) {
            case SCREEN_NORMAL:
            case SCREEN_FULLSCREEN:
                updateStartImage();
                break;
            case SCREEN_TINY:
                break;
            default:
                break;
        }
    }

    public void changeUiToPreparing() {
        switch (screen) {
            case SCREEN_NORMAL:
            case SCREEN_FULLSCREEN:
                updateStartImage();
                break;
            case SCREEN_TINY:
                break;
            default:
                break;
        }
    }


    public void changeUiToPlayingShow() {
        switch (screen) {
            case SCREEN_NORMAL:
            case SCREEN_FULLSCREEN:
                updateStartImage();
                break;
            case SCREEN_TINY:
                break;
            default:
                break;
        }

    }


    public void changeUiToPauseShow() {
        switch (screen) {
            case SCREEN_NORMAL:
            case SCREEN_FULLSCREEN:
                updateStartImage();
                break;
            case SCREEN_TINY:
                break;
            default:
                break;
        }
    }

    public void changeUiToComplete() {
        switch (screen) {
            case SCREEN_NORMAL:
            case SCREEN_FULLSCREEN:
                updateStartImage();
                break;
            case SCREEN_TINY:
                break;
            default:
                break;
        }
    }

    public void changeUiToError() {
        switch (screen) {
            case SCREEN_NORMAL:
            case SCREEN_FULLSCREEN:
                updateStartImage();
                break;
            case SCREEN_TINY:
                break;
            default:
                break;
        }

    }

    public void updateStartImage() {
        if (state == STATE_PLAYING) {
            startButton.setImageResource(R.drawable.jz_click_pause_selector);
        } else if (state == STATE_ERROR) {
        } else if (state == STATE_AUTO_COMPLETE) {
            startButton.setImageResource(R.drawable.jz_click_play_selector);
        } else if (state == STATE_NORMAL) {
            startButton.setImageResource(R.drawable.jz_click_play_selector);
        } else {
            startButton.setImageResource(R.drawable.jz_click_play_selector);
        }
    }


    @Override
    public void dismissProgressDialog() {
        super.dismissProgressDialog();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }


    @Override
    public void dismissVolumeDialog() {
        super.dismissVolumeDialog();
        if (mVolumeDialog != null) {
            mVolumeDialog.dismiss();
        }
    }


    @Override
    public void dismissBrightnessDialog() {
        super.dismissBrightnessDialog();
        if (mBrightnessDialog != null) {
            mBrightnessDialog.dismiss();
        }
    }

    public void startDismissControlViewTimer() {
        cancelDismissControlViewTimer();
        DISMISS_CONTROL_VIEW_TIMER = new Timer();
        mDismissControlViewTimerTask = new DismissControlViewTimerTask();
        DISMISS_CONTROL_VIEW_TIMER.schedule(mDismissControlViewTimerTask, 2500);
    }

    public void cancelDismissControlViewTimer() {
        if (DISMISS_CONTROL_VIEW_TIMER != null) {
            DISMISS_CONTROL_VIEW_TIMER.cancel();
        }
        if (mDismissControlViewTimerTask != null) {
            mDismissControlViewTimerTask.cancel();
        }
    }

    @Override
    public void onAutoCompletion() {
        super.onAutoCompletion();
        cancelDismissControlViewTimer();
    }

    @Override
    public void reset() {
        super.reset();
        cancelDismissControlViewTimer();
        if (clarityPopWindow != null) {
            clarityPopWindow.dismiss();
        }
    }

    public void dissmissControlView() {
        if (state != STATE_NORMAL
                && state != STATE_ERROR
                && state != STATE_AUTO_COMPLETE) {
            post(() -> {
                bottomContainer.setVisibility(View.INVISIBLE);
                topContainer.setVisibility(View.INVISIBLE);
                if (clarityPopWindow != null) {
                    clarityPopWindow.dismiss();
                }
                if (screen != SCREEN_TINY) {
                }
            });
        }
    }

    public class DismissControlViewTimerTask extends TimerTask {

        @Override
        public void run() {
            dissmissControlView();
        }
    }

    private BroadcastReceiver battertReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                int level = intent.getIntExtra("level", 0);
                int scale = intent.getIntExtra("scale", 100);
                int percent = level * 100 / scale;
                LAST_GET_BATTERYLEVEL_PERCENT = percent;
                try {
                    getContext().unregisterReceiver(battertReceiver);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };


    /**
     * 初始化数据
     *
     * @param bean
     */
    public void setMusicDate(BasePlayMusicBean bean) {
        this.mBasePlayMusicBean = bean;
        setUp(bean.getPlayUrl(), bean.getContentTitle());
        Glide.with(getContext()).load(bean.getPlayPic()).into(mMusicIv);
        mMusicTitleTv.setText(bean.getContentTitle());
        mMusicPlayCount.setText(bean.getPlayCount());
        mMusicDate.setText(bean.getPlayDate());
    }

}




















