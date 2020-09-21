package com.example.basevideodemo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.example.basevideodemo.R;
import com.example.basevideodemo.fragment.ExoMusicFragment;
import com.example.basevideodemo.fragment.ExoVideoFragment;
import com.example.basevideodemo.fragment.MusicFragment;
import com.example.basevideodemo.fragment.MyVideoFragment;
import com.example.basevideodemo.fragment.MyMusicFragment;
import com.example.basevideodemo.fragment.VideoFragment;
import com.example.basevideodemo.model.FragmentInfoBean;
import com.example.basevideodemo.widget.adapter.VideoPagerAdapter;
import com.example.basevideodemo.widget.view.NoScrollViewPager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author puyantao
 * @description
 * @date 2020/9/4 19:36
 */
public class VideoAndMusicActivity extends AppCompatActivity {
    private List<FragmentInfoBean> mFragmentInfoBeans;
    private TabLayout mTabLayout;
    private NoScrollViewPager mViewPager;
    private VideoPagerAdapter mVideoPagerAdapter;

    public static void startVideoAndMusicActivity(Activity activity) {
        Intent intent = new Intent(activity, VideoAndMusicActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_and_music);
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
        mTabLayout.setupWithViewPager(mViewPager);

        TabLayout.Tab childTab = mTabLayout.newTab();
        View view = LayoutInflater.from(this).inflate(R.layout.tab_select_item_view, mTabLayout, false);
        childTab.setCustomView(view);
        mTabLayout.addTab(childTab);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab == null || tab.getText() == null) {
                    return;
                }
                String trim = tab.getText().toString().trim();
                SpannableString spStr = new SpannableString(trim);
                StyleSpan span = new StyleSpan(Typeface.BOLD);
                spStr.setSpan(span, 0, trim.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                tab.setText(spStr);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab == null || tab.getText() == null) {
                    return;
                }
                String trim = tab.getText().toString().trim();
                SpannableString spStr = new SpannableString(trim);
                StyleSpan span = new StyleSpan(Typeface.NORMAL);
                spStr.setSpan(span, 0, trim.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                tab.setText(spStr);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void intData() {
        mFragmentInfoBeans.add(new FragmentInfoBean("自定义音频", MyMusicFragment.newInstance()));
        mFragmentInfoBeans.add(new FragmentInfoBean("自定义视频", MyVideoFragment.newInstance()));
        mFragmentInfoBeans.add(new FragmentInfoBean("Exo 音频", ExoMusicFragment.newInstance()));
        mFragmentInfoBeans.add(new FragmentInfoBean("Exo 视频", ExoVideoFragment.newInstance()));
        mFragmentInfoBeans.add(new FragmentInfoBean("JZVD 音频", MusicFragment.newInstance()));
        mFragmentInfoBeans.add(new FragmentInfoBean("JZVD 视频", VideoFragment.newInstance()));
        mVideoPagerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(3);
    }


    @Override
    public void onBackPressed() {
        Fragment fragment = mFragmentInfoBeans.get(mViewPager.getCurrentItem()).getFragment();
        if (fragment != null && fragment instanceof VideoFragment) {
            if (!((VideoFragment) fragment).onBackPressed()) {
                super.onBackPressed();
            }
        } else if (fragment != null && fragment instanceof ExoVideoFragment) {
            if (!((ExoVideoFragment) fragment).onBackPressed()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }
}








