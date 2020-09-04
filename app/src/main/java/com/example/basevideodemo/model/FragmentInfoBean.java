package com.example.basevideodemo.model;

import androidx.fragment.app.Fragment;

/**
 * @author puyantao
 * @description :
 * @date 2020/9/4
 */
public class FragmentInfoBean {
    private String title;

    private Fragment fragment;

    public FragmentInfoBean() {
    }

    public FragmentInfoBean(String title, Fragment fragment) {
        this.title = title;
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
