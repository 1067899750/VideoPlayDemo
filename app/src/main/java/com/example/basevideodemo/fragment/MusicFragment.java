package com.example.basevideodemo.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.basevideodemo.R;
import com.example.basevideodemo.model.BasePlayMusicBean;
import com.example.basevideodemo.widget.view.PlatMusicStd;

/**
 *
 * @description
 * @author puyantao
 * @date 2020/9/9 17:19
 */
public class MusicFragment extends Fragment {
    private PlatMusicStd mPlatMusicStd;

    public MusicFragment() {
        // Required empty public constructor
    }


    public static MusicFragment newInstance() {
        MusicFragment fragment = new MusicFragment();
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
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        intiView(view);
        intiData();
        return view;
    }

    private void intiView(View view) {
        mPlatMusicStd = view.findViewById(R.id.plat_music_view);
    }

    private void intiData() {
        BasePlayMusicBean musicBean = new BasePlayMusicBean();
        musicBean.setId("12121221");
        musicBean.setRow(R.raw.fly);
        musicBean.setContentTitle("视频播放器");
        musicBean.setPlayCount("播放量 1354");
        musicBean.setPlayDate("2020.09.09");
        musicBean.setLocal(true);
        mPlatMusicStd.setMusicDate(musicBean);

    }
}


















