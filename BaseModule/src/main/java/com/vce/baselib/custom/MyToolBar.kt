package com.vce.baselib.custom

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.TintTypedArray
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.getStringOrThrow
import androidx.core.view.isVisible
import com.vce.baselib.R

/**
 * 描述：MyToolBar
 * 创建者: shichengxiang
 * 创建时间：2022/8/29
 */
class MyToolBar : Toolbar {
    private var mBack: ImageView? = null
    private var mTitle: TextView? = null
    private var mRight1: TextView? = null
    private var mRight2: TextView? = null
    private var mImageView1: ImageView? = null
    private var mImageView2: ImageView? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val view = LayoutInflater.from(context).inflate(R.layout.base_title, null)
        addView(view, android.widget.Toolbar.LayoutParams.MATCH_PARENT, android.widget.Toolbar.LayoutParams.MATCH_PARENT)
        setContentInsetsAbsolute(0, 0)
        mBack = view.findViewById(R.id.iv_start)
        mTitle = view.findViewById(R.id.tv_title)
        mRight1 = view.findViewById(R.id.tv_right1)
        mRight2 = view.findViewById(R.id.tv_right2)
        mImageView1 = view.findViewById(R.id.iv_image1)
        mImageView2 = view.findViewById(R.id.iv_image2)
        if (attrs == null) return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyToolBar)
        val resBackIcon = typedArray.getDrawable(R.styleable.MyToolBar_tb_start_icon)
        val isShowBackIcon = typedArray.getBoolean(R.styleable.MyToolBar_tb_start_icon_show,true)
        val titleSize = typedArray.getDimension(R.styleable.MyToolBar_tb_title_size, 15f)
        val resTitleColor = typedArray.getResourceId(R.styleable.MyToolBar_tb_title_color, R.color.black_333333)
        val title = typedArray.getString(R.styleable.MyToolBar_tb_title_text)
//        typedArray.getStringOrThrow(R.styleable.MyToolBar_tb_end_textview1_txt)
        val txt1 = typedArray.getString(R.styleable.MyToolBar_tb_end_textview1_txt)
        val txt2 = typedArray.getString(R.styleable.MyToolBar_tb_end_textview2_txt)
        val end1Icon = typedArray.getDrawable(R.styleable.MyToolBar_tb_end_textview1_icon)
        val end2Icon = typedArray.getDrawable(R.styleable.MyToolBar_tb_end_textview2_icon)
        val color1 = typedArray.getColor(R.styleable.MyToolBar_tb_end_textview1_color,ContextCompat.getColor(context,R.color.black_333333))
        val color2 = typedArray.getColor(R.styleable.MyToolBar_tb_end_textview2_color,ContextCompat.getColor(context,R.color.black_333333))

        val image1 = typedArray.getDrawable(R.styleable.MyToolBar_tb_image1)
        val image2 = typedArray.getDrawable(R.styleable.MyToolBar_tb_image2)
        if (resBackIcon != null) {
            mBack?.setImageDrawable(resBackIcon)
        }
        mBack?.isVisible= isShowBackIcon
        mTitle?.setTextColor(ContextCompat.getColor(context, resTitleColor))
        mTitle?.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize * resources.displayMetrics.density)
        mTitle?.text = title
        if (txt1.isNullOrEmpty() && end1Icon == null) {
            mRight1?.isVisible = false
        } else {
            mRight1?.text = txt1
            mRight1?.isVisible = true
            mRight1?.setTextColor(color1)
            if (end1Icon != null) {
                end1Icon.setBounds(0, 0, end1Icon.intrinsicWidth, end1Icon.intrinsicHeight)
                mRight1?.setCompoundDrawables(null, end1Icon, null, null)
            }

        }
        if (txt2.isNullOrEmpty() && end2Icon == null) {
            mRight2?.isVisible = false
        } else {
            mRight2?.setTextColor(color2)
            mRight2?.text = if(txt2.isNullOrEmpty()) "" else txt2
            if (end2Icon != null) {
                mRight2?.isVisible = true
                end2Icon.setBounds(0, 0, end2Icon.intrinsicWidth, end2Icon.intrinsicHeight)
                mRight2?.setCompoundDrawables(null, end2Icon, null, null)
            }
        }
        if (image1 != null) {
            mImageView1?.isVisible = true
            mImageView1?.setImageDrawable(image1)
        } else {
            mImageView1?.isVisible = false
        }
        if (image2 != null) {
            mImageView2?.isVisible = true
            mImageView2?.setImageDrawable(image2)
        } else {
            mImageView2?.isVisible = false
        }

        typedArray.recycle()
    }

    fun getRight1() = mRight1
    fun getRight2() = mRight2
    fun getTvTitle() = mTitle
    fun getLeftView() = mBack
    fun setCenterTitle(title: String) {
        mTitle?.text = title
    }

    private fun init(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.base_title, null)
        addView(view)
        setContentInsetsAbsolute(0, 0)
    }
}