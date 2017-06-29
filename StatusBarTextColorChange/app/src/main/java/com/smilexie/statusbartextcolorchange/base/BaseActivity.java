package com.smilexie.statusbartextcolorchange.base;


import android.content.Context;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.smilexie.statusbartextcolorchange.R;
import com.smilexie.statusbartextcolorchange.util.Utils;
import com.smilexie.statusbartextcolorchange.databinding.ActivityBaseBinding;


/**
 * 基类
 */
public abstract class BaseActivity<SV extends ViewDataBinding> extends AppCompatActivity {
    // 布局view
    protected SV bindingView;
    protected ActivityBaseBinding mBaseBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mBaseBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_base, null, false);
        bindingView = DataBindingUtil.inflate(getLayoutInflater(), layoutResID, null, false);
        // content
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        bindingView.getRoot().setLayoutParams(params);
        RelativeLayout mContainer = (RelativeLayout) mBaseBinding.getRoot().findViewById(R.id.container);
        mContainer.addView(bindingView.getRoot());
        getWindow().setContentView(mBaseBinding.getRoot());

        Utils.setStatusBar(this, false, false);

        setToolBar();
    }

    /**
     * 设置titlebar
     */
    protected void setToolBar() {
        setSupportActionBar(mBaseBinding.toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
//            actionBar.setHomeAsUpIndicator(R.drawable.nav_icon_return);
        }
        mBaseBinding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {   //如果为true,则是键盘正在显示
                        //软件盘处于显示状态
                        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(BaseActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); //隐藏键盘
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                onBackPressed();
            }
        });
    }

    /**
     * 设置标题
     * @param text
     */
    public void setTitle(CharSequence text) {
        mBaseBinding.toolBar.setTitle("");
        mBaseBinding.titleTv.setText(text);
    }

    /**
     * 设置标题栏右侧文字
     * @param text
     */
    public void setRightTitle(CharSequence text) {
        mBaseBinding.titlebarRightTv.setText(text);
    }

    public void setLeftTitle(CharSequence text) {
        mBaseBinding.leftTitleTv.setText(text);
    }

    public void setToolbarTitle(CharSequence text) {
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBaseBinding.toolBar.setTitle(text);
        mBaseBinding.titleTv.setText("");
    }

    protected void hideBackBtn() {
        mBaseBinding.backBtn.setVisibility(View.GONE);
    }

    /**
     * 引藏标题栏
     */
    protected void hideToolBar(int variable) {
        mBaseBinding.toolBar.setVisibility(variable);
    }

    protected void hideBack(SV bindingView) {
        this.bindingView = bindingView;
    }


    /**
     * 标题栏右上角图标设置
     * @param iconRes
     */
    public void setRightBtn(int iconRes) {
        mBaseBinding.titlebarRightTv.setVisibility(View.VISIBLE);
        mBaseBinding.titlebarRightTv.setBackgroundDrawable(getResources().getDrawable(iconRes));
    }

    /**
     * 点击右侧按钮
     * @param view
     */
    public void titleRightClick(View view) {
        rightClick();
    }

    protected void rightClick(){

    }

    public void titleLeftClick(View view) {
        leftClick();
    }

    protected void leftClick(){

    }


}
