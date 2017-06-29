package com.smilexie.statusbartextcolorchange;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.smilexie.statusbartextcolorchange.databinding.ActivityPhotographerDescBinding;
import com.smilexie.statusbartextcolorchange.fragment.MinePicFragment;
import com.smilexie.statusbartextcolorchange.util.Utils;

/**
 * 个人中心页面，随着页面上滑状态栏背景和文字颜色值跟着变化
 * Created by SmileXie on 2017/6/29.
 */

public class UserInfoActivity extends AppCompatActivity {
        private MinePicFragment minePicFragment;
        private CollapsingToolbarLayoutState state = CollapsingToolbarLayoutState.EXPANDED;
        private String title = "宋佳";
        private ActivityPhotographerDescBinding bindingView;
        private int height;
        private boolean hasMeasured = false;

        private enum CollapsingToolbarLayoutState {
            EXPANDED,
            COLLAPSED,
            INTERNEDIATE
        }

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Utils.setStatusBar(this, false, true);
            bindingView = DataBindingUtil.setContentView(this, R.layout.activity_photographer_desc);
            bindingView.titleTv.setText(title);
            height = bindingView.backdrop.getHeight();
            initView();
            initFragment(savedInstanceState);
            setAppBarListener();
        }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_icon_white_return);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //设置CollapsingToolbarLayout的标题文字
//        bindingView.collapsingToolbar.setTitle(title);
        bindingView.collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.text_main_color));
    }

    /**
     * 初始化碎片
     */
    private void initFragment(Bundle savedInstanceState) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (savedInstanceState != null) {
            minePicFragment = (MinePicFragment) getSupportFragmentManager().findFragmentByTag("minePicFragment");
        } else {
            minePicFragment = new MinePicFragment();
            transaction.add(R.id.fl_body, minePicFragment, "minePicFragment");
        }
        transaction.commitAllowingStateLoss();
    }

    private void setAppBarListener() {
        measureHeight();
        bindingView.appbar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset == 0) {
                if (state != CollapsingToolbarLayoutState.EXPANDED) {
                    state = CollapsingToolbarLayoutState.EXPANDED;//修改为展开状态
                    bindingView.titleTv.setVisibility(View.GONE);
                    bindingView.toolbar.setNavigationIcon(R.drawable.nav_icon_white_return);
                    getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_VISIBLE);
                }
            } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                bindingView.titleTv.setVisibility(View.VISIBLE);
                bindingView.toolbar.setNavigationIcon(R.drawable.nav_icon_return);
                state = CollapsingToolbarLayoutState.COLLAPSED;//修改为折叠状态
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            } else {
                if (Math.abs(verticalOffset) > height) {
                    bindingView.titleTv.setVisibility(View.VISIBLE);
                    float scale =  1- height / (float) Math.abs(verticalOffset);
                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                        if (state == CollapsingToolbarLayoutState.COLLAPSED && scale < 0.55) {//由折叠变为展开
                            bindingView.toolbar.setNavigationIcon(R.drawable.nav_icon_white_return);
                            getWindow().getDecorView().setSystemUiVisibility(
                                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_VISIBLE);
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                            }
                        }

                        state = CollapsingToolbarLayoutState.INTERNEDIATE;
                    }
                    float alpha = (255 * scale);
                    Log.e("xwf", "alpha = " + alpha + " scale = " + scale);
                    bindingView.titleTv.setTextColor(Color.argb((int) alpha, 53,55,58));
//                    bindingView.toolbar.setBackgroundColor(Color.argb((int) alpha, 255,255,255));
                    bindingView.toolbar.setNavigationIcon(R.drawable.nav_icon_return);
                } else {
                    bindingView.titleTv.setVisibility(View.GONE);
                    bindingView.toolbar.setNavigationIcon(R.drawable.nav_icon_white_return);
                }
            }
        });
    }

    private void measureHeight() {
        ViewTreeObserver vto = bindingView.coordinatorlayout.getViewTreeObserver();

        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                if (hasMeasured == false) {

                    height = bindingView.toolbar.getMeasuredHeight();
                    hasMeasured = true;

                }
                return true;
            }
        });
    }
}
