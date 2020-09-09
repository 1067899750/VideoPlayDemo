package com.example.basevideodemo.widget.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.basevideodemo.R;
import com.example.basevideodemo.model.BasePlayMusicBean;

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
    private BasePlayMusicBean mBasePlayMusicBean;
    private ImageView mMusicIv;
    private TextView mMusicTitleTv;
    private TextView mMusicPlayCount;
    private TextView mMusicDate;

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
        changeUiToPlayingClear();
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
    public void changeUrl(int urlMapIndex, long seekToInAdvance) {
        super.changeUrl(urlMapIndex, seekToInAdvance);

    }

    @Override
    public void changeUrl(JZDataSource jzDataSource, long seekToInAdvance) {
        super.changeUrl(jzDataSource, seekToInAdvance);

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
            }
        }
        return super.onTouch(v, event);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.start || state == STATE_PREPARING) {
            Log.i(TAG, "onClick start [" + this.hashCode() + "] ");
            if (jzDataSource == null || jzDataSource.urlsMap.isEmpty() || jzDataSource.getCurrentUrl() == null) {
                Toast.makeText(getContext(), getResources().getString(R.string.no_url), Toast.LENGTH_SHORT).show();
                return;
            }
            if (state == STATE_NORMAL) {
                if (!jzDataSource.getCurrentUrl().toString().startsWith("file") && !
                        jzDataSource.getCurrentUrl().toString().startsWith("/") &&
                        !JZUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {//这个可以放到std中
                    showWifiDialog();
                    return;
                }
                startVideo();
            } else if (state == STATE_PLAYING) {
                Log.d(TAG, "pauseVideo [" + this.hashCode() + "] ");
                mediaInterface.pause();
                onStatePause();
            } else if (state == STATE_PAUSE) {
                mediaInterface.start();
                onStatePlaying();
            } else if (state == STATE_AUTO_COMPLETE) {
                startVideo();
            }
        }
    }


    public void onClickUiToggle() {
        if (state == STATE_PREPARING) {
            startButton.setImageResource(R.drawable.play_music);
        } else if (state == STATE_PLAYING) {
            startButton.setImageResource(R.drawable.play_music);
        } else if (state == STATE_PAUSE) {
            startButton.setImageResource(R.drawable.pause_music);
        }
    }

    @Override
    public void setScreenNormal() {
        super.setScreenNormal();
        changeStartButtonSize((int) getResources().getDimension(R.dimen.start_button_w_h_normal));
    }


    @Override
    public void setScreenFullscreen() {
        super.setScreenFullscreen();
        //进入全屏之后要保证原来的播放状态和ui状态不变，改变个别的ui
        changeStartButtonSize((int) getResources().getDimension(R.dimen.start_button_w_h_normal));
    }


    @Override
    public void setScreenTiny() {
        super.setScreenTiny();
        setAllControlsVisiblity();
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


    public void startDismissControlViewTimer() {
        cancelDismissControlViewTimer();

    }

    public void cancelDismissControlViewTimer() {

    }


    public void changeUiToPauseShow() {
        switch (screen) {
            case SCREEN_NORMAL:
                setAllControlsVisiblity();
                updateStartImage();
                break;
            case SCREEN_FULLSCREEN:
                setAllControlsVisiblity();
                updateStartImage();
                break;
            case SCREEN_TINY:
                break;
        }
    }


    public void changeUiToError() {
        switch (screen) {
            case SCREEN_NORMAL:
                setAllControlsVisiblity();
                updateStartImage();
                break;
            case SCREEN_FULLSCREEN:
                setAllControlsVisiblity();
                updateStartImage();
                break;
            case SCREEN_TINY:
                break;
        }
    }


    public void changeUiToNormal() {
        switch (screen) {
            case SCREEN_NORMAL:
                setAllControlsVisiblity();
                updateStartImage();
                break;
            case SCREEN_FULLSCREEN:
                setAllControlsVisiblity();
                updateStartImage();
                break;
            case SCREEN_TINY:
                break;
        }
    }

    public void changeUiToPlayingClear() {
        switch (screen) {
            case SCREEN_NORMAL:
                setAllControlsVisiblity();
                break;
            case SCREEN_FULLSCREEN:
                setAllControlsVisiblity();
                break;
            case SCREEN_TINY:
                break;
        }

    }

    public void changeUiToPreparing() {
        switch (screen) {
            case SCREEN_NORMAL:
            case SCREEN_FULLSCREEN:
                setAllControlsVisiblity();
                updateStartImage();
                break;
            case SCREEN_TINY:
                break;
        }
    }


    public void changeUiToComplete() {
        switch (screen) {
            case SCREEN_NORMAL:
                setAllControlsVisiblity();
                updateStartImage();
                break;
            case SCREEN_FULLSCREEN:
                setAllControlsVisiblity();
                updateStartImage();
                break;
            case SCREEN_TINY:
                break;
        }
    }


    public void changeUiToPauseClear() {
        switch (screen) {
            case SCREEN_NORMAL:
                setAllControlsVisiblity();
                break;
            case SCREEN_FULLSCREEN:
                setAllControlsVisiblity();
                break;
            case SCREEN_TINY:
                break;
        }

    }

    private void setAllControlsVisiblity() {


    }

    public void updateStartImage() {
        if (state == STATE_PLAYING) {
            startButton.setImageResource(R.drawable.jz_click_pause_selector);
        } else if (state == STATE_ERROR) {
            bottomContainer.setVisibility(VISIBLE);
        } else if (state == STATE_AUTO_COMPLETE) {
        } else if (state == STATE_NORMAL) {
            startButton.setImageResource(R.drawable.jz_click_play_selector);
        } else {
            startButton.setImageResource(R.drawable.jz_click_play_selector);
        }
    }


    @Override
    public void showProgressDialog(float deltaX, String seekTime, long seekTimePosition, String
            totalTime, long totalTimeDuration) {
        super.showProgressDialog(deltaX, seekTime, seekTimePosition, totalTime, totalTimeDuration);
        onCLickUiToggleToClear();
    }


    @Override
    public void showVolumeDialog(float deltaY, int volumePercent) {
        super.showVolumeDialog(deltaY, volumePercent);
        onCLickUiToggleToClear();
    }

    @Override
    public void dismissVolumeDialog() {
        super.dismissVolumeDialog();
    }

    @Override
    public void showBrightnessDialog(int brightnessPercent) {
        super.showBrightnessDialog(brightnessPercent);
        onCLickUiToggleToClear();
    }

    @Override
    public void dismissBrightnessDialog() {
        super.dismissBrightnessDialog();
    }


    public void onCLickUiToggleToClear() {
        if (state == STATE_PREPARING) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPreparing();
            } else {
            }
        } else if (state == STATE_PLAYING) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPlayingClear();
            } else {
            }
        } else if (state == STATE_PAUSE) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPauseClear();
            } else {
            }
        } else if (state == STATE_AUTO_COMPLETE) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToComplete();
            } else {
            }
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
    }


    @Override
    public void dismissProgressDialog() {
        super.dismissProgressDialog();

    }


    public void changeStartButtonSize(int size) {
        ViewGroup.LayoutParams lp = startButton.getLayoutParams();
        lp.height = size;
        lp.width = size;
    }

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




















