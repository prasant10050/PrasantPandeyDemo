package com.example.prasant.prasantpandeydemo;

import android.content.Context;

import com.example.prasant.prasantpandeydemo.exception.NoConnectivityException;

import java.io.IOException;

import com.example.prasant.prasantpandeydemo.utils.NetworkUtil;
import okhttp3.Interceptor;

import okhttp3.Request;
import okhttp3.Response;


public class ConnectivityInterceptor implements Interceptor {

    private Context mContext;

    public ConnectivityInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!NetworkUtil.isOnline(mContext)) {
            throw new NoConnectivityException();
        }

        Request.Builder builder = chain.request().newBuilder();
        return chain.proceed(builder.build());
    }

}