package com.learning.twilson.baking.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("https://d17h27t6h515a5.cloudfront.net/")
            .addConverterFactory(GsonConverterFactory.create());

    static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass){
        return retrofit.create(serviceClass);
    }
}
