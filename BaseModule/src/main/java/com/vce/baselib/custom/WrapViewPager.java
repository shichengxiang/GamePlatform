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

public class WrapViewPager extends ViewPager {
    private static int default_minHeight = (int) (300 * Util.INSTANCE.getMApp().getResources().getDisplayMetrics().density);
    private int viewHeight = 200;
    private int current = 0;
    private boolean noScroll = false;

    public WrapViewPager(@NonNull Context context) {
        super(context);
    }

    public WrapViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setDefaultHeight(int height) {
        if (height > default_minHeight)
            default_minHeight = height;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        Logger.d("wrapViewpager  onMeasure()");
//        int targetIndex = 0;
//        int childCnt = getChildCount();
//        int totalCnt = 0;
//        if (getAdapter() != null) totalCnt = getAdapter().getCount();
//        int curItem = getCurrentItem();
////
//        if (childCnt < getOffscreenPageLimit() * 2 + 1) {
//            if (childCnt == totalCnt) {
//                targetIndex = current;
//            } else {
//                if (curItem - getOffscreenPageLimit() < 0) {
//                    targetIndex = current;
//                } else {
//                    targetIndex = getOffscreenPageLimit();
//                }
//            }
//        } else {
//            targetIndex = 1;
//        }
        PagerAdapter adapter = getAdapter();
        if(adapter instanceof AbstractWrapViewAdapter){
            View child = ((AbstractWrapViewAdapter) adapter).getPrimaryItem();
            if (child != null) {
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                viewHeight = Math.max(child.getMeasuredHeight(), default_minHeight);
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY);
            }
        }else if(adapter instanceof AbstractWrapFragmentAdapter){
            View child = ((AbstractWrapFragmentAdapter) adapter).getPrimaryItem();
            if (child != null) {
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                viewHeight = Math.max(child.getMeasuredHeight(), default_minHeight);
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void resetHeight(int currentIndex) {
        current = currentIndex;
        if (current < getChildCount()) {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(-1, viewHeight);
            } else {
                layoutParams.height = viewHeight;
            }
            setLayoutParams(layoutParams);
        }
//        Util.log("match2Viewpager current= "+current  +"  height="+viewHeight);
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
