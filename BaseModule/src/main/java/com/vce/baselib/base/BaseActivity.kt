package com.vce.baselib.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.vce.baselib.R
import com.vce.baselib.utils.WaitDialog
import java.lang.reflect.ParameterizedType

/**
 * 描述：BaseActivity
 * 创建者: shichengxiang
 * 创建时间：2022/8/2
 */
abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {
    var bind: T? = null
    var mVm: BaseViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val clz = type.actualTypeArguments[0] as Class<T>
            val method = clz.getMethod("inflate", LayoutInflater::class.java)
            bind = method.invoke(null, layoutInflater) as T
        }
        setContentView(bind?.root)
        val ivBack = bind?.root?.findViewById<View>(R.id.iv_start)
        ivBack?.setOnClickListener { onBackPressed() }
        initView(savedInstanceState)
        loadData()
    }

    abstract fun initView(bundle: Bundle?)
    abstract fun loadData()

    /**
     * 获取viewmodel 必须用这个才能保证省事哦
     */
    fun <T : BaseViewModel> getModel(modelClass: Class<T>): T {
        val m = ViewModelProvider(this).get(modelClass)
        mVm = m
        mVm?.callback?.observe(this) {
            when (it.first) {
                BaseViewModel.TAG_DISMISS_LOADING -> dismissLoading()
                BaseViewModel.TAG_SHOW_LOADING -> showLoading()
                BaseViewModel.TAG_FINISH_ACTIVITY -> finish()
            }
        }
        return m
    }

    fun showLoading() {
        if (!isRunning()) return
        if (mLoadingDialog == null) {
            mLoadingDialog = WaitDialog(this)
        }
        if (mLoadingDialog?.isShowing == false) mLoadingDialog?.show()
    }

    fun dismissLoading() {
        if (isFinishing || isDestroyed) return
        mLoadingDialog?.dismiss()
    }

    fun isRunning() = !(isFinishing || isDestroyed)

    private var mLoadingDialog: Dialog? = null
}