package com.tas.hash.zvchain.model;

import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class BodyRewardTxs {
    private String hash;
    private String block_hash;
    private String groupId;
    private List<String> targetIds;
    private BigInteger value;
    private BigInteger packFee;
    private String statusReport;
    private boolean success;
}
