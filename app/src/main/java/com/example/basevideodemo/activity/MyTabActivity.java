package com.example.basevideodemo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.basevideodemo.R;
import com.example.basevideodemo.model.FragmentInfoBean;
import com.example.basevideodemo.widget.adapter.VideoPagerAdapter;
import com.example.basevideodemo.widget.view.NoScrollViewPager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MyTabActivity extends AppCompatActivity {
    private List<FragmentInfoBean> mFragmentInfoBeans;
    private TabLayout mTabLayout;
    private NoScrollViewPager mViewPager;
    private VideoPagerAdapter mVideoPagerAdapter;


    public static void startMyTabActivity(Activity activity) {
        Intent intent = new Intent(activity, MyTabActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tab);
        initView();
        intData();
    }

    private void initView() {
        mFragmentInfoBeans = new ArrayList<>();
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.content_vp);
        mVideoPagerAdapter = new VideoPagerAdapter(getSupportFragmentManager(), mFragmentInfoBeans);
        mViewPager.setOffscreenPageLimit(mVideoPagerAdapter.getCount());
        mViewPager.setCurrentItem(0);
        mViewPager.setAdapter(mVideoPagerAdapter);



    }

    private void intData() {


    }


}










