package com.example.basevideodemo.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.basevideodemo.R;
import com.example.basevideodemo.model.BaseVideoBean;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;


/**
 * @author puyantao
 * @description 视频播放器
 * @date 2020/9/4 20:00
 */
public class VideoFragment extends Fragment {
    private Context mContext;
    private JzvdStd mPlayVideo;

    public VideoFragment() {
        // Required empty public constructor
    }


    public static VideoFragment newInstance() {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_video, container, false);
        mPlayVideo = rootView.findViewById(R.id.jz_video);
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        BaseVideoBean bean = new BaseVideoBean();
        bean.setId("212121");
        bean.setTitle("饺子快长大");
        bean.setVideoUrl("http://jzvd.nathen.cn/342a5f7ef6124a4a8faf00e738b8bee4/cf6d9db0bd4d41f59d09ea0a81e918fd-5287d2089db37e62345123a1be272f8b.mp4");
        bean.setVideoPic("http://jzvd-pic.nathen.cn/jzvd-pic/1bb2ebbe-140d-4e2e-abd2-9e7e564f71ac.png");

        mPlayVideo.setUp(
                "http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4"
                , "饺子闭眼睛");
        Glide.with(mContext).load("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640").into(mPlayVideo.thumbImageView);

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            Jzvd.resetAllVideos();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        Jzvd.resetAllVideos();
    }
}












