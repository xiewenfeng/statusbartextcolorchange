package com.smilexie.statusbartextcolorchange.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.smilexie.statusbartextcolorchange.R;
import com.smilexie.statusbartextcolorchange.util.Utils;
import com.smilexie.statusbartextcolorchange.base.BaseFragment;
import com.smilexie.statusbartextcolorchange.databinding.FragmentActiveBinding;

/**
 * 活动页面
 * Created by SmileXie on 2017/6/29.
 */

public class ActiveFragment extends BaseFragment<FragmentActiveBinding> {

    @Override
    public int setContent() {
        return R.layout.fragment_active;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initState();
    }

    /**
     * 动态的设置状态栏  实现沉浸式状态栏
     *
     */
    private void initState() {
        bindingView.llBar.setVisibility(View.VISIBLE);
        //获取到状态栏的高度
        int statusHeight = Utils.getStatusBarHeight(getActivity());
        //动态的设置隐藏布局的高度
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bindingView.llBar.getLayoutParams();
        params.height = statusHeight;
        bindingView.llBar.setLayoutParams(params);
    }
}
