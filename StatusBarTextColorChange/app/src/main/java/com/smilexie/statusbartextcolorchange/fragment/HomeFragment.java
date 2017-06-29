package com.smilexie.statusbartextcolorchange.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.smilexie.statusbartextcolorchange.R;
import com.smilexie.statusbartextcolorchange.base.BaseFragment;
import com.smilexie.statusbartextcolorchange.databinding.FragmentHomeBinding;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 * Created by SmileXie on 2017/6/29.
 */

public class HomeFragment extends BaseFragment<FragmentHomeBinding> {
    @Override
    public int setContent() {
        return R.layout.fragment_home;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setBanner();
    }

    private void setBanner() {
        List<String> imageUrl = new ArrayList<>();
        imageUrl.add("http://pic11.nipic.com/20101121/11417_191053007953_2.jpg");
        imageUrl.add("http://pic.58pic.com/58pic/13/61/25/90T58PICvY4_1024.jpg");
        imageUrl.add("http://img17.3lian.com/d/file/201701/05/9254a500f06833241bb54aac2b7f7d52.jpg");
        bindingView.banner.setImages(imageUrl)//设置图片集合
                .setDelayTime(3000)//设置轮播时间
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)//设置指示器位置（当banner模式中有指示器时）
                .setIndicatorGravity(BannerConfig.RIGHT)
                .start();//banner设置方法全部调用完毕时最后调用
    }
}