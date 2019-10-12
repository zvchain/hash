package com.zv.hash.zvchain.api;

import com.zv.hash.zvchain.Response;
import com.zv.hash.zvchain.ZvRequest;
import com.zv.hash.zvchain.model.MinerInfo;
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
    Call<Response<com.zv.hash.zvchain.model.BlockDetail>> requestBlockDetail(@Body ZvRequest jsonObject);

    @POST("/")
    Call<Response<com.zv.hash.zvchain.model.BlockHeader>> requestBlockHeader(@Body ZvRequest jsonObject);

    @POST("/")
    Call<Response<String>> requestTx(@Body ZvRequest jsonObject);

    @POST("/")
    Call<Response<com.zv.hash.zvchain.model.TxReceive>> requestReceipt(@Body ZvRequest jsonObject);

    @POST("/")
    Call<Response<MinerInfo>> getMinerInfo(@Body ZvRequest jsonObject);

    @POST("/")
    Call<Response<com.zv.hash.zvchain.model.BlockHeightReward>> getBlockHeightRewardInfo(@Body ZvRequest jsonObject);

    @POST("/")
    Call<Response<com.zv.hash.zvchain.model.MinerPoolInfo>> getMinerPoolInfo(@Body ZvRequest jsonObject);
}
