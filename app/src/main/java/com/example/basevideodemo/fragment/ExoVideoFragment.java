package com.example.basevideodemo.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.basevideodemo.R;

/**
 *
 * @description exoplayer2
 * @author puyantao
 * @date 2020/9/10 16:32
 */
public class ExoVideoFragment extends Fragment {

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
        intiView();
        initData();
        return v;
    }

    private void intiView() {


    }

    private void initData() {


    }


}













