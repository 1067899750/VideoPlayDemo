package com.example.basevideodemo.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.basevideodemo.R;
import com.example.basevideodemo.model.BaseVideoBean;
import com.example.basevideodemo.widget.exo.video.ExoPlayVideoView;
import com.example.basevideodemo.widget.exo.video.ExoVideoBean;
import com.example.basevideodemo.widget.view.PlatVideoStd;

/**
 * @author puyantao
 * @description exoplayer2
 * @date 2020/9/10 16:32
 */
public class ExoVideoFragment extends Fragment {
    private ExoPlayVideoView mExoPlayVideoView;
    private ImageView mExoBgIv;

    public ExoVideoFragment() {
    }

    public static ExoVideoFragment newInstance() {
        ExoVideoFragment fragment = new ExoVideoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_exo_video, container, false);
        intiView(v);
        initData();
        return v;
    }

    private void intiView(View v) {
        mExoPlayVideoView = v.findViewById(R.id.exo_play_video);
        mExoBgIv = v.findViewById(R.id.video_iv);
    }

    private void initData() {
        ExoVideoBean bean = new ExoVideoBean();
        bean.setId("212121");
        bean.setTitle("饺子快长大");
        bean.setVideoUrl("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4");
        bean.setVideoPic("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
        mExoPlayVideoView.setUp(bean);
//        Glide.with(getContext()).load(bean.getVideoPic()).into(mExoBgIv);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && mExoPlayVideoView != null){
            mExoPlayVideoView.stopPlay();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mExoPlayVideoView.onDestroy();
    }

    /**
     * 用于返回是否需要实现监听
     */
    public boolean onBackPressed() {
        if (mExoPlayVideoView != null) {
            if (mExoPlayVideoView.screen == PlatVideoStd.SCREEN_FULLSCREEN) {
                //推出全屏
                mExoPlayVideoView.backPress();
                return true;
            }
        }
        return false;
    }


}













