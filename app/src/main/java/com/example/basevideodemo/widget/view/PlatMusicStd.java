package com.example.basevideodemo.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.basevideodemo.R;
import com.example.basevideodemo.model.BasePlayMusicBean;

import java.util.Timer;
import java.util.TimerTask;

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
    private RelativeLayout mPlayMusicRl;
    private ImageView mControlMusicIv;
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
    public void init(Context context) {
        super.init(context);
        //图片
        mMusicIv = findViewById(R.id.music_iv);
        mPlayMusicRl = findViewById(R.id.play_music_rl);
        //控制是否播放
        mControlMusicIv = findViewById(R.id.control_music_iv);
        //标题
        mMusicTitleTv = findViewById(R.id.music_title_tv);
        //播放量
        mMusicPlayCount = findViewById(R.id.music_play_count);
        //日期
        mMusicDate = findViewById(R.id.music_date);
        mPlayMusicRl.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.plat_music_layout_std;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        if (id == R.id.bottom_seek_progress) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    cancelDismissControlViewTimer();
                    break;
                case MotionEvent.ACTION_UP:
                    startDismissControlViewTimer();
                    break;
            }
        }
        return super.onTouch(v, event);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.play_music_rl) {
            if (jzDataSource == null || jzDataSource.urlsMap.isEmpty() || jzDataSource.getCurrentUrl() == null) {
                //播放地址无效
                Toast.makeText(getContext(), getResources().getString(R.string.no_url), Toast.LENGTH_SHORT).show();
                return;
            }
            if (state == STATE_NORMAL) {
                if (!jzDataSource.getCurrentUrl().toString().startsWith("file") &&
                        !jzDataSource.getCurrentUrl().toString().startsWith("/") &&
                        !JZUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
                    //使用移动网络播放
                    showWifiDialog();
                    return;
                }
                //启动视频播放
                startVideo();
            } else if (state == STATE_AUTO_COMPLETE) {
                onClickUiToggle();
            }

        }
    }


    public void onClickUiToggle() {
        if (state == STATE_PREPARING) {
            mControlMusicIv.setImageResource(R.drawable.play_music);
        } else if (state == STATE_PLAYING) {
            mControlMusicIv.setImageResource(R.drawable.play_music);
        } else if (state == STATE_PAUSE) {
            mControlMusicIv.setImageResource(R.drawable.pause_music);
        }
    }

    public void startDismissControlViewTimer() {
        cancelDismissControlViewTimer();

    }

    public void cancelDismissControlViewTimer() {

    }


    /**
     * 初始化数据
     *
     * @param bean
     */
    public void setMusicDate(BasePlayMusicBean bean) {
        this.mBasePlayMusicBean = bean;
        Glide.with(getContext()).load(bean.getPlayPic()).into(mMusicIv);
        mMusicTitleTv.setText(bean.getContentTitle());
        mMusicPlayCount.setText(bean.getPlayCount());
        mMusicDate.setText(bean.getPlayDate());
    }

}




















