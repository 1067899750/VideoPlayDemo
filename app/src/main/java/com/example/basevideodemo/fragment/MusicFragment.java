package com.example.basevideodemo.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.basevideodemo.R;
import com.example.basevideodemo.model.BasePlayMusicBean;
import com.example.basevideodemo.until.GetAssetsFiles;
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
        musicBean.setContentTitle("饺子快长大");
        musicBean.setPlayUrl("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4");
        musicBean.setPlayPic("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
        musicBean.setPlayCount("播放量 1354");
        musicBean.setPlayDate("2020.09.09");
        mPlatMusicStd.setMusicDate(musicBean);



    }

}


















