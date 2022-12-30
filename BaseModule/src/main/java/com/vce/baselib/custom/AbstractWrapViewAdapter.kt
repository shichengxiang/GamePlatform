package com.android.vfinance.custom

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

/**
 * 基于viewpager 都可以用
 */
abstract class AbstractWrapViewAdapter : PagerAdapter {

    var mViews = SparseArray<View>()
    var mTitles= arrayOf<String>()
    var mCurrentView:View?=null

    constructor(titles:Array<String>) : super() {
        mTitles=titles
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_UNCHANGED
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitles[position]
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
        mCurrentView=`object` as View
    }

    fun getPrimaryItem():View?=mCurrentView

    override fun getCount() = mTitles.size
}