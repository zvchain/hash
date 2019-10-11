package com.tas.hash.zvchain.model;

import lombok.Data;

import java.math.BigInteger;

@Data
public class Transaction {
    private String data;
    private Double value;
    private Integer nonce;
    private String source;
    private String target;
    private Integer type;
    private BigInteger gasLimit;
    private BigInteger gasPrice;
    private String hash;
    private String extraData;
}
