package com.vce.baselib.network;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class MyGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    MyGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        JsonReader jsonReader = gson.newJsonReader(value.charStream());
        try {
            T result = adapter.read(jsonReader);
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonIOException("JSON document was not fully consumed.");
            }
            return result;
        } finally {
            value.close();
        }
    }
//    @Override
//    public T convert(ResponseBody value) throws IOException {
//
//        String json = value.string();
//        //第一次解析
//        BaseResponse obj = gson.fromJson(json, BaseResponse.class);
//        if (obj.getCode() != 200) {
//            //如果是服务端返回的错误码，则抛出自定义异常
////            throw new ApiException(obj.getStatus(), obj.getMsg());
//            return (T) obj;
//        }
//        //第二次解析
//        T  result = adapter.fromJson(json);
//        value.close();
//        return result;
//    }
}