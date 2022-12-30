package com.vce.baselib.network

/**
 * 描述：BaseResponse
 * 创建者: shichengxiang
 * 创建时间：2022/3/23
 */
open class BaseResponse<T> {
    var code: Int = 0
    var msg: String? = null
    var data: T? = null
    var count:Int?=null
    constructor()
    constructor(code: Int, msg: String) {
        this.code = code
        this.msg = msg
        this.data = null
    }

    constructor(code: Int, msg: String, data: T) {
        this.code = code
        this.msg = msg
        this.data = data
    }
}