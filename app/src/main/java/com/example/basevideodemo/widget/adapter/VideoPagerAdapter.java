package com.example.basevideodemo.widget.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.basevideodemo.model.FragmentInfoBean;

import java.util.List;

/**
 * @author puyantao
 * @description :
 * @date 2020/9/4
 */
public class VideoPagerAdapter extends FragmentStatePagerAdapter {
    private List<FragmentInfoBean> mFragmentInfoList;

    public VideoPagerAdapter(@NonNull FragmentManager fm, List<FragmentInfoBean> beans) {
        super(fm);
        this.mFragmentInfoList = beans;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentInfoList.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return mFragmentInfoList == null ? 0 : mFragmentInfoList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentInfoList.get(position).getTitle();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }
}














