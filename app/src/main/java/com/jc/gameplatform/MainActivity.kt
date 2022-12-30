package com.jc.gameplatform

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.jc.gameplatform.databinding.ActivityMainBinding
import com.vce.baselib.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private var mImageAdapter = ImageAdapter()
    override fun initView(bundle: Bundle?) {
        bind?.rv?.apply {
            layoutManager = GridLayoutManager(context,3)
            adapter = mImageAdapter
        }
    }

    override fun loadData() {
    }
}