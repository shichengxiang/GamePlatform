package com.vce.baselib.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewDebug
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.vce.baselib.log
import com.vce.baselib.utils.WaitDialog
import java.lang.reflect.ParameterizedType

/**
 * 描述：BaseFragment
 * 创建者: shichengxiang
 * 创建时间：2022/8/2
 */
abstract class BaseFragment<T:ViewBinding> :Fragment(){
    var bind:T?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val type = javaClass.genericSuperclass
        if(type is ParameterizedType){
            val clz = type.actualTypeArguments[0] as Class<T>
            val method = clz.getMethod("inflate", LayoutInflater::class.java,ViewGroup::class.java,Boolean::class.java)
            bind = method.invoke(null,inflater,container,false) as T
        }
        return bind?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }
    abstract fun initView(root: View)
    abstract fun onLeave()
    abstract fun onSelected()
    fun showLoading() {
        if (isDetached) return
        if (mLoadingDialog == null) {
            mLoadingDialog = WaitDialog(context)
        }
        if (mLoadingDialog?.isShowing == false) mLoadingDialog?.show()
    }

    fun dismissLoading() {
        if (isDetached) return
        mLoadingDialog?.dismiss()
    }


    private var mLoadingDialog: Dialog? = null

}