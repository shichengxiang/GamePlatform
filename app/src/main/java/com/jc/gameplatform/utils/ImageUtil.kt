package com.jc.gameplatform.utils

import android.graphics.Bitmap

/**
 * 描述：ImageUtil
 * 创建者: shichengxiang
 * 创建时间：2022/12/30
 */
object ImageUtil {
    /**
     * 分割图片
     */
    fun splitBitmap(res:Bitmap,row:Int,col:Int):List<Bitmap>{
        val result = mutableListOf<Bitmap>()
        val width = res.width
        val height = res.height
        val pWidth = width / col
        val pHeight = height / row
        for (i in 0..row){
            for (j in 0..col){
//                val index = i+j
                val xValue = j*pWidth
                val yValue = i*pHeight
                result.add(Bitmap.createBitmap(res,xValue,yValue,pWidth,pHeight))
            }
        }
        return result
    }
}