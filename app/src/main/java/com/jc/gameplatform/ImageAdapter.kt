package com.jc.gameplatform

import android.graphics.Bitmap
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * 描述：ImageAdapter
 * 创建者: shichengxiang
 * 创建时间：2022/12/30
 */
class ImageAdapter :BaseQuickAdapter<Bitmap,BaseViewHolder>(R.layout.item_rv_image) {
    override fun convert(holder: BaseViewHolder, item: Bitmap) {
        val imageView = holder.getView<ImageView>(R.id.imageView)
    }
}