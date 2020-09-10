package com.example.basevideodemo.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.basevideodemo.R;
import com.example.basevideodemo.model.BasePlayMusicBean;
import com.example.basevideodemo.widget.view.BaseMusicPlayView;

/**
 * @author puyantao
 * @description 音乐播放器
 * @date 2020/9/4 20:00
 */
public class MyMusicFragment extends Fragment {
    private BaseMusicPlayView mBaseMusicPlayView;

    public MyMusicFragment() {
        // Required empty public constructor
    }

    public static MyMusicFragment newInstance() {
        MyMusicFragment fragment = new MyMusicFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_music_my, container, false);
        intiView(rootView);
        intiData();
        return rootView;
    }

    private void intiView(View rootView) {
        mBaseMusicPlayView = rootView.findViewById(R.id.music_play_view);

    }

    private void intiData() {
        try {
            BasePlayMusicBean musicBean = new BasePlayMusicBean();
            musicBean.setId("12121221");
            musicBean.setRow(R.raw.fly);
//            musicBean.setPlayUrl("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4");
            musicBean.setPlayPic("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
            musicBean.setContentTitle("视频播放器");
            musicBean.setPlayCount("播放量 1354");
            musicBean.setPlayDate("2020.09.09");
            musicBean.setLocal(true);
            mBaseMusicPlayView.setPlayDate(musicBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser){
            if (mBaseMusicPlayView != null) {
                mBaseMusicPlayView.setMusicPause();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //音频
        mBaseMusicPlayView.onDestroy();
    }

}











