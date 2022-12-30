package com.vce.baselib.network

import com.google.gson.Gson
import com.vce.baselib.log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.net.URLDecoder
import java.nio.charset.Charset

/**
 * 描述：HeadInterceptor
 * 创建者: shichengxiang
 * 创建时间：2022/3/23
 */
class HeadInterceptor :Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request=chain.request().newBuilder()
//            .header()
            .build()
        log("http==start =${getRequestInfo(request)}")
        var response=chain.proceed(request)
        var newToken=response.header("token")
        log(String.format(
                "http==end =---->Retrofit\n请求链接：%s\n请求方式：%s\n请求token：%s\n请求参数：%s\n请求响应:%s",
                response.toString(),
                request.method,
                request.headers["token"],
                getRequestInfo(request),
                getResponseInfo(response)))
        return response
    }
    private fun getRequestInfo(request: Request):String{
        var str = ""
        if (request == null) {
            return str
        }
        val requestBody = request.body ?: return str
        try {
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            //将返回的数据URLDecoder解码
            str = buffer.readUtf8()
            str = str.replace("%(?![0-9a-fA-F]{2})".toRegex(), "%25")
            str = URLDecoder.decode(str, "utf-8")
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return str
    }
    private fun getResponseInfo(response:Response):String{
        var str = ""
        if (response == null || !response.isSuccessful) {
            return str!!
        }
        val responseBody = response.body
        val contentLength = responseBody!!.contentLength()
        val source = responseBody!!.source()
        try {
            source.request(Long.MAX_VALUE) // Buffer the entire body.
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val buffer = source.buffer()
        val charset = Charset.forName("utf-8")
        if (contentLength != 0L) {
            str = buffer.clone().readString(charset)
        }
        return str
    }
}