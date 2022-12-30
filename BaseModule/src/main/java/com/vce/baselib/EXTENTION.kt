package com.vce.baselib

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.vce.baselib.network.BaseResponse

/**
 * 描述：Extention
 * 创建者: shichengxiang
 * 创建时间：2022/3/25
 */
const val TAG = "baselib"
fun log(msg:String){
    Log.d(TAG,msg)
}
fun logE(msg:String){
    Log.e(TAG,msg)
}
fun dp2px(d:Int):Int{
    return (Resources.getSystem().displayMetrics.density*d).toInt()
}