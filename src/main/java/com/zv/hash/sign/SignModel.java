package com.zv.hash.sign;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author lt
 * @Date 5:26 下午 2019/10/10
 */
@Data
public class SignModel {

    private String data = "";
    private String extraData = "";
    private BigInteger gasLimit = BigInteger.valueOf(3000);
    private BigInteger gasPrice = BigInteger.valueOf(500);
    private BigInteger nonce;
    private String source;
    private String target;
    private Byte type = (byte) 0;
    private BigDecimal value;

}
