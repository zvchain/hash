package com.zv.hash.zvchain;

import com.zv.hash.sign.SignModel;
import com.zv.hash.sign.SignUtil;
import com.zv.hash.zvchain.api.ZvApi;
import com.zv.hash.zvchain.model.*;
import retrofit2.Call;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lt
 * @Date 5:29 下午 2019/10/11
 */
public class ChainBaseService {

    static ConcurrentHashMap<String, String> keyMap = new ConcurrentHashMap<>(8);

    static ZvApi zvApi;

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

    public BlockHeader getBlockByHash(String hash) throws IOException {
        ZvRequest zvRequest = new ZvRequest();
        zvRequest.setMethod("Gzv_getBlockByHash");
        zvRequest.getParams().add(hash);
        Call<Response<BlockHeader>> response = zvApi.requestBlockDetail(zvRequest);
        return response.execute().body().getResult();
    }

    public BlockHeader getBlockByHeight(long height) throws IOException {
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
        String zvcAddress = signModel.getSource();
        String privateKey = keyMap.get(zvcAddress.toLowerCase());
        if (privateKey == null) {
            throw new RuntimeException("please import the private key of the source address!");
        }
        if (modelNonce == null) {
            nonce = getNonce(zvcAddress);
        } else {
            nonce = modelNonce.longValue();
        }
        signModel.setNonce(BigInteger.valueOf(nonce));
        signModel.setValue(signModel.getValue().multiply(BigDecimal.TEN.pow(9)));
        String signResult = SignUtil.sign(privateKey, signModel);
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

}
