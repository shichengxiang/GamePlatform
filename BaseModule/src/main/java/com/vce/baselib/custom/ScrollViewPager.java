package com.vce.baselib.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.vfinance.custom.AbstractWrapFragmentAdapter;
import com.android.vfinance.custom.AbstractWrapViewAdapter;
import com.vce.baselib.utils.Util;

public class ScrollViewPager extends ViewPager {
    private int current = 0;
    private boolean noScroll = false;

    public ScrollViewPager(@NonNull Context context) {
        super(context);
    }

    public ScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (noScroll) {
            return false;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (noScroll) return false;
        return super.onInterceptTouchEvent(ev);
    }

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }
}
