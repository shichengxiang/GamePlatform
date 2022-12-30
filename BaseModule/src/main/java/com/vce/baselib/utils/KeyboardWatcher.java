package com.vce.baselib.utils;

import android.app.Activity;
import android.app.Application;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

public final class KeyboardWatcher implements
        ViewTreeObserver.OnGlobalLayoutListener,
        Application.ActivityLifecycleCallbacks {

    private WeakReference<Activity> mActivity;
    private View mContentView;
    @Nullable
    private SoftKeyboardStateListener mListeners;
    private boolean mSoftKeyboardOpened;
    private int mStatusBarHeight;
    private int navHeight;

    public static KeyboardWatcher with(Activity activity) {
        return new KeyboardWatcher(activity);
    }

    private KeyboardWatcher(Activity activity) {
        mActivity = new WeakReference(activity);
        mContentView = activity.findViewById(Window.ID_ANDROID_CONTENT);
        mContentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (mActivity.get() != null)
                mActivity.get().registerActivityLifecycleCallbacks(this);
        } else {
            if (mActivity.get() != null)
                mActivity.get().getApplication().registerActivityLifecycleCallbacks(this);
        }
        mContentView.getViewTreeObserver().addOnGlobalLayoutListener(this);

        // 获取 status_bar_height 资源的 ID
        calcBarHeight();
    }
    private void calcBarHeight(){
        // 获取 status_bar_height 资源的 ID
        if (mActivity.get() != null){
            int resourceId = mActivity.get().getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                //根据资源 ID 获取响应的尺寸值
                mStatusBarHeight = mActivity.get().getResources().getDimensionPixelSize(resourceId);
            }
//            Util.log("keyboard== "+Util.isNavigationBarExist(mActivity.get()) +"   height=="+Util.getNavigationBarHeight(mActivity.get()));
            if (Util.isNavigationBarExist(mActivity.get())) {
                navHeight = Util.getNavigationBarHeight(mActivity.get());
            }
        }
    }

    /**
     * {@link ViewTreeObserver.OnGlobalLayoutListener}
     */

    @Override
    public void onGlobalLayout() {
        final Rect r = new Rect();
        //r will be populated with the coordinates of your view that area still visible.
        mContentView.getWindowVisibleDisplayFrame(r);
        final int heightDiff = mContentView.getRootView().getHeight() - (r.bottom - r.top);
        if (!mSoftKeyboardOpened && heightDiff > mContentView.getRootView().getHeight() / 4) {
            mSoftKeyboardOpened = true;
            if (mListeners == null) {
                return;
            }
            if (mActivity.get() != null)
                if ((mActivity.get().getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != WindowManager.LayoutParams.FLAG_FULLSCREEN) {
//                    Util.log("statusbarHeight= "+ mStatusBarHeight  +"  navigationHeight= "+navHeight);
                    mListeners.onSoftKeyboardOpened(heightDiff - mStatusBarHeight-navHeight);
                } else {
//                    Util.log("else height ="+heightDiff);
                    mListeners.onSoftKeyboardOpened(heightDiff);
                }
        } else if (mSoftKeyboardOpened && heightDiff < mContentView.getRootView().getHeight() / 4) {
            mSoftKeyboardOpened = false;
            if (mListeners == null) {
                return;
            }
            mListeners.onSoftKeyboardClosed();
        }
    }

    /**
     * 设置软键盘弹出监听
     */
    public void setListener(@Nullable SoftKeyboardStateListener listener) {
        mListeners = listener;
    }

    /**
     * {@link Application.ActivityLifecycleCallbacks}
     */

    @Override
    public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        if (mActivity.get() == activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (mActivity.get() != null)
                    mActivity.get().unregisterActivityLifecycleCallbacks(this);
            } else {
                if (mActivity.get() != null)
                    mActivity.get().getApplication().unregisterActivityLifecycleCallbacks(this);
            }
            mContentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            mActivity = null;
            mContentView = null;
            mListeners = null;
        }
    }

    /**
     * 软键盘状态监听器
     */
    public interface SoftKeyboardStateListener {

        /**
         * 软键盘弹出了
         *
         * @param keyboardHeight 软键盘高度
         */
        void onSoftKeyboardOpened(int keyboardHeight);

        /**
         * 软键盘收起了
         */
        void onSoftKeyboardClosed();
    }
}