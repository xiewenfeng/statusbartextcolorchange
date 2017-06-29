# statusbartextcolorchange
android状态栏背景及文字变色
android中沉浸式状态栏的文章已经满大街了，可是在实现某些效果时，还是得各种搜索，测试一通后，最后还常常满足不了要求，即使好不容易在一部手机上满足了需求，放在另外一手机上，发现效果还各种不适配。今天把自己这几天学到的关于沉浸式状态栏知识进行总结下。

实现的效果图。

下面是同一个activity切换不同fragment时，状态栏文字颜色跟着变化的效果图：

![同一个activity切换不同fragment时，状态栏文字颜色跟着变化](http://img.blog.csdn.net/20170622132213288?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvc21pbGVpYW0=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

下图是同一个Activity向上滚动时，标题栏和状态栏文字颜色根据变化的效果：

![Activity向上滚动时，标题栏和状态栏文字颜色根据变化](http://img.blog.csdn.net/20170622132354164?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvc21pbGVpYW0=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

##1. 实现秀明状态栏常规方法
```java
protected boolean useThemestatusBarColor = false;//是否使用特殊的标题栏背景颜色，android5.0以上可以设置状态栏背景色，如果不使用则使用透明色值
    protected boolean useStatusBarColor = true;//是否使用状态栏文字和图标为暗色，如果状态栏采用了白色系，则需要使状态栏和图标为暗色，android6.0以上可以设置
    
protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //根据上面设置是否对状态栏单独设置颜色
            if (useThemestatusBarColor) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorTheme));
            } else {
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !withoutUseStatusBarColor) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
```
在Activity布局的根节点处加上android:fitsSystemWindows="true"属性就可以了，要不布局会跑到状态栏和导航栏下面，与导航栏和状态栏重叠，这当然不是我们希望的。

Activity通过上面的设置，可以实现如下效果：

![状态栏文字颜色和图标为黑色](http://img.blog.csdn.net/20170622142620061?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvc21pbGVpYW0=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)


上面设置状态栏文字颜色和图标为暗色主要采用了以下两个标志：
```java
//设置状态栏文字颜色及图标为深色
getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
```

 - View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN 是从API 16开始启用，实现效果：
视图延伸至状态栏区域，状态栏悬浮于视图之上
 - View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR 是从 API 23开始启用，实现效果：
设置状态栏图标和状态栏文字颜色为深色，为适应状态栏背景为浅色调，该Flag只有在使用了FLAG_DRWS_SYSTEM_BAR_BACKGROUNDS，并且没有使用FLAG_TRANSLUCENT_STATUS时才有效，即只有在透明状态栏时才有效。

##2. 同一个Activity包含多个Fragment时，如何实现不同fragment的状态栏背景和文字颜色不一样
如下面的效果图：
![同一个activity切换不同fragment时，状态栏文字颜色跟着变化](http://img.blog.csdn.net/20170622132213288?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvc21pbGVpYW0=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

就是设置了状态栏为暗色后，还得设置回来，这其实主要靠下面两个flag标识，结全上面的两个flag标识就能实现。

```java
//设置状态栏文字颜色及图标为浅色
getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
```
 - View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN 前面说过了，是为了让视图能延伸到状态栏区域，使状态栏悬浮在视图布局之上。
 - View.SYSTEM_UI_FLAG_LAYOUT_STABLE
保持整个View稳定, 常和控制System UI悬浮, 隐藏的Flags共用, 使View不会因为System UI的变化而重新layout。
 
 将上面的代码放在不同fragment切换处即可实现上面的效果了：
```java
private void switchTo(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position) {            
            case 0://首页                
            hideShowFragment(transaction, fourFragment, thirdFragment, secondFragment, homeFragment);//展示第一个fragment
getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                getWindow().getDecorView().findViewById(android.R.id.content).setPadding(0, 0, 0, CommonUtils.navigationHeight);
                break;
          
            case 1:  //活动                
            hideShowFragment(transaction, homeFragment, thirdFragment, fourFragment, secondFragment);//展示第二个fragment
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//实现状态栏图标和文字颜色为暗色                     
getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
                getWindow().getDecorView().findViewById(android.R.id.content).setPadding(0, 0, 0, CommonUtils.navigationHeight);
                break;
       case 2:  //所有图片    
hideShowFragment(transaction, homeFragment, fourFragment, secondFragment, thirdFragment);
//展示第三个fragment
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//实现状态栏图标和文字颜色为暗色                          
getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
                getWindow().getDecorView().findViewById(android.R.id.content).setPadding(0, 0, 0, CommonUtils.navigationHeight);
                break;
            
            case 3://我的
              hideShowFragment(transaction, homeFragment, secondFragment, thirdFragment, fourFragment);//展示第四个fragment
 //实现状态栏图标和文字颜色为浅色                     
 getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                getWindow().getDecorView().findViewById(android.R.id.content).setPadding(0, 0, 0, CommonUtils.navigationHeight);
                break;
            default:
                break;
        }

    }

//fragment切换实现
private void hideShowFragment(FragmentTransaction transaction, Fragment fragment1, Fragment fragment2, Fragment fragment3, Fragment fragment4) {
        transaction.hide(fragment1);
        transaction.hide(fragment2);
        transaction.hide(fragment3);
        transaction.show(fragment4);
        transaction.commitAllowingStateLoss();
    }
```
大家可能会注意到，我这里切换每个fragment时，有下面这样一行代码：
```java
getWindow().getDecorView().findViewById(android.R.id.content).setPadding(0, 0, 0, CommonUtils.navigationHeight);
```
这行代码干什么用的，因为我们这里首页和我的页面，需要背景图片填充到状态栏，故不能使用android:fitsSystemWindows属性，故在实现上面效果时带有底部导航栏手机上就会存在一个大坑，解决办法见第3章节。同时不使用android:fitsSystemWindows属性，怎么让布局不遮挡状态栏文字，解决办法见第4章节。
##3. 带有底部导航栏手机底部导航按钮会和navigationbar重叠

如下图所示：
![底部导航栏与底部按钮重叠](http://img.blog.csdn.net/20170622142700999?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvc21pbGVpYW0=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

全屏时，由于视图布局会填充到状态栏和导航栏下方，如果不使用android:fitsSystemWindows="true"属性，就会使底部导航栏和应用底部按钮重叠，导视按钮点击失效，这该怎么办？
经过网上搜索相关资料，其实实现方法和实现透明状态栏效果方法一致。
解决的方法：
1. 先判断手机是否有物理按钮判断是否存在NavigationBar；
2. 计算底部的NavigationBar高度；
3. 最后设置视图边距。

###3.1 通过反射判断手机是否有物理按钮NavigationBar
```java
//判断是否存在NavigationBar
public static boolean checkDeviceHasNavigationBar(Context context) {
    boolean hasNavigationBar = false;
    Resources rs = context.getResources();
    int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
    if (id > 0) {
        hasNavigationBar = rs.getBoolean(id);
    }
    try {
        Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
        Method m = systemPropertiesClass.getMethod("get", String.class);
        String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
        if ("1".equals(navBarOverride)) {
            hasNavigationBar = false;
        } else if ("0".equals(navBarOverride)) {
            hasNavigationBar = true;
        }
    } catch (Exception e) {

    }
    return hasNavigationBar;

}
```
###3.2  计算底部的NavigationBar高度
```java
/**
 * 获取底部导航栏高度
 * @return
 */
public static int getNavigationBarHeight(Context context) {
    Resources resources = context.getResources();
    int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
    //获取NavigationBar的高度
    navigationHeight = resources.getDimensionPixelSize(resourceId);
    return navigationHeight;
}
```
###3.3 设置视图边距
```java
getWindow().getDecorView().findViewById(android.R.id.content).setPadding(0, 0, 0, CommonUtils.navigationHeight);
```
通过上面的设置，会使布局距离底部导航栏的高度。
最后实现效果如下：
![导航栏与底部按钮不重叠](http://img.blog.csdn.net/20170622142728547?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvc21pbGVpYW0=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)


参考文章：[android 6.0导航栏 NavigationBar影响视图解决办法](http://www.jianshu.com/p/b8488acb18f1)
##4. 不使用fiySystemWindow属性，布局怎么能不遮挡状态栏文字
跟第三章节类似，在主页中，需要使布局中带文字的布局向上margin状态栏的高度。
###4.1 先在布局中设置一个占空LinearLayout
我们先来看看第三个Fragment的布局实现
```java
<layout xmlns:android="http://schemas.android.com/apk/res/android">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">
        <!--这个是隐藏的布局，然后通过动态的设置高度达到效果-->
        <LinearLayout
            android:id="@+id/ll_bar"
            android:layout_width="fill_parent"
            android:layout_height="1dp"
            android:orientation="vertical"
            android:background="@color/white"
            android:visibility="gone">
        </LinearLayout>

        <RelativeLayout
            android:layout_width="match_parent"
            android:layout_height="@dimen/toollbar_height"
            android:background="@drawable/topbar_generic">
            <TextView
                android:id="@+id/title_tv"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_centerInParent="true"
                android:layout_gravity="center"
                android:textColor="@color/text_main_color"
                android:text="@string/all_photo"
                android:textSize="20sp" />
            <TextView
                android:id="@+id/titlebar_right_tv"
                style="@style/main_content_text_style"
                android:layout_alignParentRight="true"
                android:layout_margin="@dimen/margin_10dp"
                android:gravity="center_vertical"
                android:text="@string/confirm"/>
        </RelativeLayout>
        <FrameLayout
            android:id="@+id/fmImageList"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="@android:color/white" />
    </LinearLayout>
</layout>
```
###4.2 在代码中动态设置占空布局高度
```java
/**
 * 动态的设置状态栏  实现沉浸式状态栏
 */
private void initState() {

    //当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        //透明状态栏
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        bindingView.llBar.setVisibility(View.VISIBLE);
        //获取到状态栏的高度
        int statusHeight = CommonUtils.getStatusBarHeight(getActivity());
        //动态的设置隐藏布局的高度
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bindingView.llBar.getLayoutParams();
        params.height = statusHeight;
        bindingView.llBar.setLayoutParams(params);
       }
}

/**
 * 通过反射的方式获取状态栏高度
 *
 * @return
 */
public static int getStatusBarHeight(Context context) {
    try {
        Class<?> c = Class.forName("com.android.internal.R$dimen");
        Object obj = c.newInstance();
        Field field = c.getField("status_bar_height");
        int x = Integer.parseInt(field.get(obj).toString());
        return context.getResources().getDimensionPixelSize(x);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return 0;
}
```
对于上面的第二个和第三个fragment的实现，为了让视图布局不遮挡状态栏文字，主要是通过先给界面设置占位布局，然后在代码中动态设置该布局为状态栏高度，这其实就是让状态栏悬浮在这个占空布局上面。视图布局位于占空布局下方，从而达到视图布局不遮挡状态栏效果。
上面对于版本的判断，如果android版本大于4.4, 则让该布局显示出来，而版本低于4.4, 由于没有沉浸式状态栏效果，则不需要给界面设置占空布局。

而对于第一个首页和第四个我的fragment，则需要布局的图片填充到状态栏底下，而标题栏要位于状态栏下方，这其实只需要一种取巧实现，一般手机状态栏高度都是在25dp左右，当然在代码中动态获取状态栏高度，动态设置也可以。我这里是简单实现，让标题栏marginTop状态栏高度即可，对于android不同版本，可以如下设置。
对于values中dimens.xml设置状态栏的高度：
```java
<dimen name="status_bar_height">0dp</dimen>
```
对于values-v19中dimens.xml设置状态栏的高度：
```java
<dimen name="status_bar_height">25dp</dimen>
```

##5. 同一个Activity上下滑动动态变换标题栏和状态栏文字字体色值
 效果如下：
 ![Activity向上滚动时，标题栏和状态栏文字颜色根据变化](http://img.blog.csdn.net/20170622132354164?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvc21pbGVpYW0=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
 
 这种布局实现主要是依靠CoordinatorLayout+AppBarLayout+CollapsingToolbarLayout+Toolbar+NestedScrollView来实现，之前我也写过类似的博文来介绍CoordinatorLayout的使用方法。感兴趣的小伙伴可以参下：[android沉浸式状态栏、fitsSystemWindows、标题栏折叠](http://blog.csdn.net/smileiam/article/details/61643006)
下面我们说说怎么在界面滑动时，修改状态栏和标题栏文字颜色。
这个主要通过监听AppBarLayout滑动的距离，向上滑动，如果大于标题栏的高度，则要动态改变标题栏文字颜色，当标题栏折叠时，改变状态栏文字颜色及返回铵钮图标，同时状态栏文字颜色变成暗色。
向下滑动时，随着标题栏慢慢消失，需要把状态栏文字颜色变成浅色调。
```java
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
                                      bindingView.titleTv.setTextColor(Color.argb((int) alpha, 53,55,58));

                    bindingView.toolbar.setNavigationIcon(R.drawable.nav_icon_return);
                } else {
                    bindingView.titleTv.setVisibility(View.GONE);
                    bindingView.toolbar.setNavigationIcon(R.drawable.nav_icon_white_return);
                }
            }
        });
    }

//获取标题栏高度
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
```
总结：
根据android提供的widnow的flag，状态栏浅色调和深色调，我们可以实时动态变换一个Activity的状态栏颜色，同时结合CoordinatorLayout，我们可以实现更加复杂的效果。
