package com.cbd.android.retrofit;

import com.cbd.android.common.Utils;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer " + Utils.getToken()).build();
        return chain.proceed(request);
    }
}
