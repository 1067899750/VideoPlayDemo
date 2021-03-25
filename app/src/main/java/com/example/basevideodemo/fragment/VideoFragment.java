package com.example.basevideodemo.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.basevideodemo.R;
import com.example.basevideodemo.model.BaseVideoBean;
import com.example.basevideodemo.widget.view.PlatVideoStd;

import cn.jzvd.Jzvd;


/**
 * @author puyantao
 * @description 视频播放器
 * @date 2020/9/4 20:00
 */
public class VideoFragment extends Fragment {
    private Context mContext;
    private PlatVideoStd mPlayVideo;

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
        View rootView = inflater.inflate(R.layout.fragment_video_my, container, false);
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
        bean.setVideoUrl("https://yyttest.purang.com/group1/M00/00/D3/Cgpgml__3zmAWteqAPS355jgrk0139.mp4");
        bean.setVideoPic("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");

        mPlayVideo.setUp(bean.getVideoUrl(), bean.getTitle());
        Glide.with(mContext).load(bean.getVideoPic()).into(mPlayVideo.thumbImageView);

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

    /**
     * 用于返回是否需要实现监听
     */
    public boolean onBackPressed() {
        if (mPlayVideo != null) {
            if (mPlayVideo.screen == PlatVideoStd.SCREEN_FULLSCREEN) {
                //推出全屏
                PlatVideoStd.backPress();
                return true;
            }
        }
        return false;
    }

}












