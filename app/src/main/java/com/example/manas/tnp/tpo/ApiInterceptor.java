package com.example.manas.tnp.tpo;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiInterceptor implements Interceptor {

    final static String accessToken = "963a5a83b3f8b7f838b6f86160debb2ad74e91a2";
    final static String AUTHORIZATION = "token";

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        Request originalRequest = chain.request();

        // Add authorization header with updated authorization value to intercepted request
        Request authorisedRequest = originalRequest.newBuilder()
                .header(AUTHORIZATION, accessToken)
                .build();
        return chain.proceed(authorisedRequest);
    }
}
