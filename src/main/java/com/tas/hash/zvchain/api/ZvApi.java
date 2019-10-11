package com.tas.hash.zvchain.api;

import com.tas.hash.zvchain.Response;
import com.tas.hash.zvchain.ZvRequest;
import com.tas.hash.zvchain.model.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ZvApi {
    @POST("/")
    Call<Response<Integer>> requestInteger(@Body ZvRequest jsonObject);

    @POST("/")
    Call<Response<Long>> requestLong(@Body ZvRequest jsonObject);

    @POST("/")
    Call<Response<Double>> requestDouble(@Body ZvRequest jsonObject);

    @POST("/")
    Call<Response<BlockDetail>> requestBlockDetail(@Body ZvRequest jsonObject);

    @POST("/")
    Call<Response<BlockHeader>> requestBlockHeader(@Body ZvRequest jsonObject);

    @POST("/")
    Call<Response<String>> requestTx(@Body ZvRequest jsonObject);

    @POST("/")
    Call<Response<TxReceive>> requestReceipt(@Body ZvRequest jsonObject);

    @POST("/")
    Call<Response<MinerInfo>> getMinerInfo(@Body ZvRequest jsonObject);

    @POST("/")
    Call<Response<BlockHeightReward>> getBlockHeightRewardInfo(@Body ZvRequest jsonObject);

    @POST("/")
    Call<Response<MinerPoolInfo>> getMinerPoolInfo(@Body ZvRequest jsonObject);
}
