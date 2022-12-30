package com.vce.baselib.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import com.vce.baselib.R
import com.vce.baselib.utils.Util

/**
 * 描述：BaseDialog
 * 创建者: shichengxiang
 * 创建时间：2022/11/30
 */
abstract class BaseDialog :AlertDialog {
    private var useDefaultLayout = true
    private var mAnimStyle = -1
    constructor(context: Context, @LayoutRes layoutId:Int,theme:Int? = R.style.BaseDialogStyle):super(context,theme?:R.style.BaseDialogStyle){
        val root = LayoutInflater.from(context).inflate(layoutId,null)
        setView(root)
        initView(root)
    }
    abstract fun initView(root:View)
    override fun show() {
        super.show()
        if(useDefaultLayout){
            window?.apply {
                setWindowAnimations(R.style.NormalDialogAnim)
                setLayout((Util.getScreenWidth() * 0.7).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
            }
        }
    }
    fun useDefaultLayout(use:Boolean){
        useDefaultLayout = use
    }
    fun setAnim(@IdRes anim:Int){
        mAnimStyle = anim
    }
}