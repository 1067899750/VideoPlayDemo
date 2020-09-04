package com.example.basevideodemo.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.basevideodemo.R;

/**
 * @author puyantao
 * @description 音频 + 视频
 * @date 2020/9/4 20:28
 */
public class MusicAndVideoFragment extends Fragment {

    public MusicAndVideoFragment() {
        // Required empty public constructor
    }

    public static MusicAndVideoFragment newInstance() {
        MusicAndVideoFragment fragment = new MusicAndVideoFragment();
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
        return inflater.inflate(R.layout.fragment_music_and_video, container, false);
    }
}