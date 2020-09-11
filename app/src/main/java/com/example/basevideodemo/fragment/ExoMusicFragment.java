package com.example.basevideodemo.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.basevideodemo.R;
import com.example.basevideodemo.model.BasePlayMusicBean;
import com.example.basevideodemo.widget.exo.music.ExoMusicBean;
import com.example.basevideodemo.widget.exo.music.ExoPlayerMusicView;

/**
 * @author puyantao
 * @description exoplayer2
 * @date 2020/9/10 14:35
 */
public class ExoMusicFragment extends Fragment {
    private ExoPlayerMusicView mMusicControlView;

    public ExoMusicFragment() {
        // Required empty public constructor
    }

    public static ExoMusicFragment newInstance() {
        ExoMusicFragment fragment = new ExoMusicFragment();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_exo_music, container, false);
        mMusicControlView = v.findViewById(R.id.exo_music_view);
        intiView();
        initData();
        return v;
    }

    private void intiView() {


    }

    private void initData() {
        ExoMusicBean bean = new ExoMusicBean();
        bean.setId("12121221");
        bean.setPlayUrl("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4");
        bean.setPlayPic("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
        bean.setContentTitle("视频播放器");
        bean.setPlayCount("播放量 1354");
        bean.setPlayDate("2020.09.09");
        mMusicControlView.initData(bean);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && mMusicControlView != null){
            mMusicControlView.stopPlay();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMusicControlView != null){
            mMusicControlView.onDestroy();
        }
    }
}

















