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
public class MyVideoFragment extends Fragment {

    public MyVideoFragment() {
        // Required empty public constructor
    }

    public static MyVideoFragment newInstance() {
        MyVideoFragment fragment = new MyVideoFragment();
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
        return inflater.inflate(R.layout.fragment_music_my, container, false);
    }
}