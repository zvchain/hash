package com.zv.hash.zvchain.model;

import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class GenRewardTx {
    private String hash;
    private String blockHash;
    private String groupId;
    private List<String> targetIds;
    private BigInteger value;
    private BigInteger pack_fee;
    private String statusReport;
    private boolean success;

}
