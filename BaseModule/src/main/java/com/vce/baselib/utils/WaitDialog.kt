package com.vce.baselib.utils

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.GridLayout
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.vce.baselib.R
import com.vce.baselib.dp2px

/**
 * 加载中弹窗
 */
class WaitDialog(context: Context?) : BaseDialog(context!!, R.layout.layout_loading, R.style.NormalDialogAnim) {
    override fun initView(root: View) {
        setCancelable(true)
        setCanceledOnTouchOutside(false)
        useDefaultLayout(false)
    }
    override fun show() {
        super.show()
        window?.apply {
            attributes = attributes.apply {
                gravity = Gravity.CENTER
                width = dp2px(120)
                height = dp2px(120)
            }
//            setLayout(dp2px(120), dp2px(120))
        }
    }
}