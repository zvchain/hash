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
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ChainUtilsService {

    private static volatile ChainUtilsService chainUtilsService;

    private static ZvApi zvApi;

    private static final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    private ChainUtilsService() {
        InputStream in = ClassLoader.getSystemResourceAsStream("zvc.properties");
        Properties p = new Properties();
        try {
            p.load(in);
        } catch (IOException ignored) {
            throw new RuntimeException("请在zvc.properties中配置zvc节点地址");
        }
        String baseUrl = p.getProperty("zvc.chain.url");
        if (baseUrl == null) {
            throw new RuntimeException("请在zvc.properties中配置zvc节点地址");
        }
        OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        zvApi = retrofit.create(ZvApi.class);
    }

    public static ChainUtilsService getInstance() {
        if (chainUtilsService == null) {
            synchronized (ChainUtilsService.class) {
                if (chainUtilsService == null) {
                    chainUtilsService = new ChainUtilsService();
                }
            }
        }
        return chainUtilsService;
    }

    public Long getBlockHeight() throws IOException {
        ZvRequest zvRequest = new ZvRequest();
        zvRequest.setMethod("Gzv_blockHeight");
        Call<Response<Long>> response = zvApi.requestLong(zvRequest);
        return response.execute().body().getResult();
    }

    public Long getNonce(String address) throws IOException {
        ZvRequest zvRequest = new ZvRequest();
        zvRequest.setMethod("Gzv_nonce");
        zvRequest.getParams().add(address);
        Call<Response<Long>> response = zvApi.requestLong(zvRequest);
        return response.execute().body().getResult();
    }

    public BlockDetail getBlockDetailByHeight(long height) throws IOException {
        ZvRequest zvRequest = new ZvRequest();
        zvRequest.setMethod("Dev_blockDetailByHeight");
        zvRequest.getParams().add(height);
        Call<Response<BlockDetail>> response = zvApi.requestBlockDetail(zvRequest);
        return response.execute().body().getResult();
    }

    public BlockDetail getBlockDetailByHash(String hash) throws IOException {
        ZvRequest zvRequest = new ZvRequest();
        zvRequest.setMethod("Dev_blockDetail");
        zvRequest.getParams().add(hash);
        Call<Response<BlockDetail>> response = zvApi.requestBlockDetail(zvRequest);
        return response.execute().body().getResult();
    }

    public BlockHeader getBlockHeaderByHeight(long height) throws IOException {
        ZvRequest zvRequest = new ZvRequest();
        zvRequest.setMethod("Gzv_getBlockByHeight");
        zvRequest.getParams().add(height);
        Call<Response<BlockHeader>> response = zvApi.requestBlockHeader(zvRequest);
        return response.execute().body().getResult();
    }

    private String sendTx(TxSend txSend) throws IOException {
        ZvRequest zvRequest = new ZvRequest();
        zvRequest.setMethod("Gzv_tx");
        zvRequest.getParams().add(txSend.marshal());
        Call<Response<String>> response = zvApi.requestTx(zvRequest);
        return response.execute().body().getResult();
    }

    public String sendTx(SignModel signModel) throws Exception {
        BigInteger modelNonce = signModel.getNonce();
        Long nonce;
        String zvcAddress = SignUtil.getAddress(signModel.getPrivateKey());
        if (modelNonce == null) {
            nonce = getNonce(zvcAddress);
        } else {
            nonce = modelNonce.longValue();
        }
        signModel.setNonce(BigInteger.valueOf(nonce));
        signModel.setValue(signModel.getValue().multiply(BigInteger.TEN.pow(9)));
        String signResult = SignUtil.sign(signModel);
        TxSend txSend = TxSend.builder()
                .sign(signResult)
                .source(zvcAddress)
                .target(signModel.getTarget())
                .value(signModel.getValue().longValue())
                .gasLimit(signModel.getGasLimit().longValue())
                .gasPrice(signModel.getGasPrice().longValue())
                .txType(signModel.getType().intValue())
                .nonce(nonce)
                .data(signModel.getData().getBytes())
                .extraData(signModel.getExtraData().getBytes())
                .build();
        return sendTx(txSend);
    }

    public TxReceive getReceipt(String hash) throws IOException {
        ZvRequest zvRequest = new ZvRequest();
        zvRequest.setMethod("Gzv_txReceipt");
        zvRequest.getParams().add(hash);
        Call<Response<TxReceive>> response = zvApi.requestReceipt(zvRequest);
        return response.execute().body().getResult();
    }

    public Double getBalance(String address) throws IOException {
        ZvRequest zvRequest = new ZvRequest();
        zvRequest.setMethod("Gzv_balance");
        zvRequest.getParams().add(address);
        Call<Response<Double>> response = zvApi.requestDouble(zvRequest);
        return response.execute().body().getResult();
    }

    public MinerInfo getMinerInfo(String address, String detail) throws IOException {
        ZvRequest zvRequest = new ZvRequest();
        zvRequest.setMethod("Gzv_minerInfo");
        zvRequest.getParams().add(address);
        zvRequest.getParams().add(detail);
        Call<Response<MinerInfo>> response = zvApi.getMinerInfo(zvRequest);
        return response.execute().body().getResult();
    }

    public BlockHeightReward getBlockHeightRewardInfo(long height) throws IOException {
        ZvRequest zvRequest = new ZvRequest();
        zvRequest.setMethod("Explorer_explorerBlockReward");
        zvRequest.getParams().add(height);
        Call<Response<BlockHeightReward>> response = zvApi.getBlockHeightRewardInfo(zvRequest);
        return response.execute().body().getResult();
    }

    public MinerPoolInfo getMinerPoolInfo(String addr, Long height) throws IOException {
        ZvRequest zvRequest = new ZvRequest();
        zvRequest.setMethod("Gzv_minerPoolInfo");
        zvRequest.getParams().add(addr);
        zvRequest.getParams().add(height);
        Call<Response<MinerPoolInfo>> response = zvApi.getMinerPoolInfo(zvRequest);
        return response.execute().body().getResult();
    }
}
