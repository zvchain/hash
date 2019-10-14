package com.zv.hash.zvchain.api;

import com.zv.hash.zvchain.Response;
import com.zv.hash.zvchain.ZvRequest;
import com.zv.hash.zvchain.model.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ZvApi {

    @POST("/")
    Call<Response<Long>> requestLong(@Body ZvRequest jsonObject);

    @POST("/")
    Call<Response<Double>> requestDouble(@Body ZvRequest jsonObject);

    @POST("/")
    Call<Response<BlockHeader>> requestBlockDetail(@Body ZvRequest jsonObject);

    @POST("/")
    Call<Response<BlockHeader>> requestBlockHeader(@Body ZvRequest jsonObject);

    @POST("/")
    Call<Response<String>> requestTx(@Body ZvRequest jsonObject);

    @POST("/")
    Call<Response<TxReceive>> requestReceipt(@Body ZvRequest jsonObject);

    @POST("/")
    Call<Response<MinerInfo>> getMinerInfo(@Body ZvRequest jsonObject);

}
