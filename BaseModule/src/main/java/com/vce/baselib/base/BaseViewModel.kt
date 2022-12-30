package com.vce.baselib.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vce.baselib.utils.Util

/**
 * 描述：BaseViewModel
 * 创建者: shichengxiang
 * 创建时间：2021/12/28
 */
open class BaseViewModel : ViewModel() {
    private var currentCount = 0

    companion object {
        @JvmStatic
        val RESULT_SUCCESS = 1

        @JvmStatic
        val RESULT_FAIL = 0

        val TAG_DISMISS_LOADING = 1 //关闭loading
        val TAG_SHOW_LOADING=2 //show loading
        val TAG_FINISH_ACTIVITY=3 //
    }

    var callback = MutableLiveData<Pair<Int, Int>>() //回调ui

    /**
     * 默认隐藏loading dialog
     */
    fun dismissLoading() {
        callback.postValue(Pair(TAG_DISMISS_LOADING, currentCount++))
    }
    fun showLoading(){
        callback.postValue(Pair(TAG_SHOW_LOADING, currentCount++))
    }
    fun toast(txt:String){
        Util.toast(txt)
    }
    fun finish(){
        callback.postValue(Pair(TAG_FINISH_ACTIVITY,currentCount++))
    }

    override fun onCleared() {
        super.onCleared()
        dismissLoading()
    }
}