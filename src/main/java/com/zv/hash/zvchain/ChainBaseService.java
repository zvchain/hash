package com.zv.hash.zvchain;

import com.zv.hash.sign.SignModel;
import com.zv.hash.sign.SignUtil;
import com.zv.hash.zvchain.api.ZvApi;
import com.zv.hash.zvchain.model.MinerInfo;
import retrofit2.Call;

import java.io.IOException;
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
        com.zv.hash.zvchain.ZvRequest zvRequest = new com.zv.hash.zvchain.ZvRequest();
        zvRequest.setMethod("Gzv_blockHeight");
        Call<com.zv.hash.zvchain.Response<Long>> response = zvApi.requestLong(zvRequest);
        return response.execute().body().getResult();
    }

    public Long getNonce(String address) throws IOException {
        com.zv.hash.zvchain.ZvRequest zvRequest = new com.zv.hash.zvchain.ZvRequest();
        zvRequest.setMethod("Gzv_nonce");
        zvRequest.getParams().add(address);
        Call<com.zv.hash.zvchain.Response<Long>> response = zvApi.requestLong(zvRequest);
        return response.execute().body().getResult();
    }

    public com.zv.hash.zvchain.model.BlockDetail getBlockDetailByHeight(long height) throws IOException {
        com.zv.hash.zvchain.ZvRequest zvRequest = new com.zv.hash.zvchain.ZvRequest();
        zvRequest.setMethod("Dev_blockDetailByHeight");
        zvRequest.getParams().add(height);
        Call<com.zv.hash.zvchain.Response<com.zv.hash.zvchain.model.BlockDetail>> response = zvApi.requestBlockDetail(zvRequest);
        return response.execute().body().getResult();
    }

    public com.zv.hash.zvchain.model.BlockDetail getBlockDetailByHash(String hash) throws IOException {
        com.zv.hash.zvchain.ZvRequest zvRequest = new com.zv.hash.zvchain.ZvRequest();
        zvRequest.setMethod("Dev_blockDetail");
        zvRequest.getParams().add(hash);
        Call<com.zv.hash.zvchain.Response<com.zv.hash.zvchain.model.BlockDetail>> response = zvApi.requestBlockDetail(zvRequest);
        return response.execute().body().getResult();
    }

    public com.zv.hash.zvchain.model.BlockHeader getBlockHeaderByHeight(long height) throws IOException {
        com.zv.hash.zvchain.ZvRequest zvRequest = new com.zv.hash.zvchain.ZvRequest();
        zvRequest.setMethod("Gzv_getBlockByHeight");
        zvRequest.getParams().add(height);
        Call<com.zv.hash.zvchain.Response<com.zv.hash.zvchain.model.BlockHeader>> response = zvApi.requestBlockHeader(zvRequest);
        return response.execute().body().getResult();
    }

    private String sendTx(com.zv.hash.zvchain.model.TxSend txSend) throws IOException {
        com.zv.hash.zvchain.ZvRequest zvRequest = new com.zv.hash.zvchain.ZvRequest();
        zvRequest.setMethod("Gzv_tx");
        zvRequest.getParams().add(txSend.marshal());
        Call<com.zv.hash.zvchain.Response<String>> response = zvApi.requestTx(zvRequest);
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
        signModel.setValue(signModel.getValue().multiply(BigInteger.TEN.pow(9)));
        String signResult = SignUtil.sign(privateKey, signModel);
        com.zv.hash.zvchain.model.TxSend txSend = com.zv.hash.zvchain.model.TxSend.builder()
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

    public com.zv.hash.zvchain.model.TxReceive getReceipt(String hash) throws IOException {
        com.zv.hash.zvchain.ZvRequest zvRequest = new com.zv.hash.zvchain.ZvRequest();
        zvRequest.setMethod("Gzv_txReceipt");
        zvRequest.getParams().add(hash);
        Call<com.zv.hash.zvchain.Response<com.zv.hash.zvchain.model.TxReceive>> response = zvApi.requestReceipt(zvRequest);
        return response.execute().body().getResult();
    }

    public Double getBalance(String address) throws IOException {
        com.zv.hash.zvchain.ZvRequest zvRequest = new com.zv.hash.zvchain.ZvRequest();
        zvRequest.setMethod("Gzv_balaSignUtilnce");
        zvRequest.getParams().add(address);
        Call<com.zv.hash.zvchain.Response<Double>> response = zvApi.requestDouble(zvRequest);
        return response.execute().body().getResult();
    }

    public com.zv.hash.zvchain.model.MinerInfo getMinerInfo(String address, String detail) throws IOException {
        com.zv.hash.zvchain.ZvRequest zvRequest = new com.zv.hash.zvchain.ZvRequest();
        zvRequest.setMethod("Gzv_minerInfo");
        zvRequest.getParams().add(address);
        zvRequest.getParams().add(detail);
        Call<com.zv.hash.zvchain.Response<MinerInfo>> response = zvApi.getMinerInfo(zvRequest);
        return response.execute().body().getResult();
    }

    public com.zv.hash.zvchain.model.BlockHeightReward getBlockHeightRewardInfo(long height) throws IOException {
        com.zv.hash.zvchain.ZvRequest zvRequest = new com.zv.hash.zvchain.ZvRequest();
        zvRequest.setMethod("Explorer_explorerBlockReward");
        zvRequest.getParams().add(height);
        Call<com.zv.hash.zvchain.Response<com.zv.hash.zvchain.model.BlockHeightReward>> response = zvApi.getBlockHeightRewardInfo(zvRequest);
        return response.execute().body().getResult();
    }

    public com.zv.hash.zvchain.model.MinerPoolInfo getMinerPoolInfo(String addr, Long height) throws IOException {
        com.zv.hash.zvchain.ZvRequest zvRequest = new ZvRequest();
        zvRequest.setMethod("Gzv_minerPoolInfo");
        zvRequest.getParams().add(addr);
        zvRequest.getParams().add(height);
        Call<Response<com.zv.hash.zvchain.model.MinerPoolInfo>> response = zvApi.getMinerPoolInfo(zvRequest);
        return response.execute().body().getResult();
    }

}
