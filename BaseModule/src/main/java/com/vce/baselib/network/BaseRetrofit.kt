package com.vce.baselib.network

import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * 描述：BaseRetrofit
 * 创建者: shichengxiang
 * 创建时间：2022/3/23
 */
class BaseRetrofit<T> {
    private val DEFAULT_TIME = 10L
    var retrofit: Retrofit? = null
    private var server: T? = null
    private var tClass: Class<T>? = null
    private var baseHost :String = ""
    private var mOkHttpClient:OkHttpClient?=null
    private var mInterceptor:Interceptor?=null

    constructor(tClass: Class<T>) {
        this.tClass = tClass
    }
    fun setHost(host:String):BaseRetrofit<T>{
        this.baseHost = host
        return this
    }
    fun addInterceptor(interceptor:Interceptor):BaseRetrofit<T>{
        mInterceptor = interceptor
        return this
    }
    fun init():BaseRetrofit<T> {
        //调度器
        var dispatcher = Dispatcher()
        dispatcher.maxRequests = 10
        dispatcher.maxRequestsPerHost = 10
        //拦截器
//        var headInterceptor = HeadInterceptor()
        mOkHttpClient = OkHttpClient.Builder()
            .protocols(Collections.singletonList(Protocol.HTTP_1_1))
            .readTimeout(DEFAULT_TIME, TimeUnit.SECONDS)
            .connectTimeout(DEFAULT_TIME, TimeUnit.SECONDS)
            .callTimeout(DEFAULT_TIME, TimeUnit.SECONDS)
//            .addInterceptor(headInterceptor)
            .addInterceptor(mInterceptor?:HeadInterceptor())
            .dispatcher(dispatcher)
            .build()

        retrofit = Retrofit.Builder()
            .client(mOkHttpClient)
            .addConverterFactory(MyGsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .baseUrl(baseHost)
            .build()
        return this
    }

    public fun getServer(): T {
        if (server == null) {
            server = retrofit?.create(tClass)
        }
        return server!!
    }
}