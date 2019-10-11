package com.tas.hash.zvchain;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tas.hash.sign.SignUtil;
import com.tas.hash.sign.SignModel;
import com.tas.hash.zvchain.api.ZvApi;
import com.tas.hash.zvchain.model.*;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ChainService extends ChainBaseService {

    private static volatile ChainService chainService;

    private static final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    private ChainService(String baseUrl) {
        OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        zvApi = retrofit.create(ZvApi.class);
    }

    public static ChainService getInstance(String baseUrl) {
        if (chainService == null) {
            synchronized (ChainService.class) {
                if (chainService == null) {
                    chainService = new ChainService(baseUrl);
                }
            }
        }
        return chainService;
    }


}
