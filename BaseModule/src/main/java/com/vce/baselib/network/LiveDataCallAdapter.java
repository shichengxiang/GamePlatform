package com.vce.baselib.network;

import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;

public class LiveDataCallAdapter<T> implements CallAdapter<T, LiveData<T>> {

    private Type mResponseType;

    LiveDataCallAdapter(Type mResponseType) {
        this.mResponseType = mResponseType;
    }

    @NotNull
    @Override
    public Type responseType() {
        return mResponseType;
    }

    @NotNull
    @Override
    public LiveData<T> adapt(@NotNull final Call<T> call) {
        return new com.vce.baselib.network.MyLiveData<>(call);
    }

//    private static class MyLiveData<T> extends LiveData<T> {
//
//        private final Call<T> call;
//        private AtomicBoolean stared = new AtomicBoolean(false);
//
//        MyLiveData(Call<T> call) {
//            this.call = call;
//        }
//
//        @Override
//        protected void onActive() {
//            super.onActive();
//            //确保执行一次
//            if (stared.compareAndSet(false, true)) {
//
//                call.enqueue(new Callback<T>() {
//                    @Override
//                    public void onResponse(@NotNull Call<T> call, @NotNull Response<T> response) {
//                        T body = response.body();
//                        if (200 == response.code()) {
//                                postValue(body);
//                        } else {
//                            BaseResponse apiResponse = new BaseResponse(response.code(), response.message(), null);
//                            postValue((T) apiResponse);
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(@NotNull Call<T> call, @NotNull Throwable t) {
//                        postValue((T) new BaseResponse<>(-1, t.getMessage()));
//
//                    }
//                });
//            }
//        }
//        void cancel(){
//            call.cancel();
//        }
//    }
}