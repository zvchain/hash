package com.zv.hash.zvchain.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Builder;
import lombok.Data;
import sun.misc.BASE64Encoder;

@Data
@Builder
public class TxSend {
    private String sign;
    private String source;
    private String target;
    private Long value;
    private Long gasLimit;
    private Long gasPrice;
    private Integer txType;
    private Long nonce;
    private String data;
    private String extraData;

    public static byte[] strToByteArray(String str) {
        if (str == null)
            return null;
        byte[] byteArray = str.getBytes();
        return byteArray;
    }

    private String encode(byte[] data) {
        if (data == null)
            return null;
        return new BASE64Encoder().encode(data);
    }

    public JSONObject marshal() {
        JSONObject json = new JSONObject();
        json.put("sign", this.sign);
        json.put("source",source);
        json.put("target", this.target);
        json.put("value", this.value);
        json.put("gas_limit", this.gasLimit);
        json.put("gas_price", this.gasPrice);
        json.put("type", this.txType);
        json.put("nonce", this.nonce);
        json.put("data", this.data);
        json.put("extra_data", this.extraData);
        return json;
    }
}
