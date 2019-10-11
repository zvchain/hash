package com.tas.hash.zvchain.model;

import lombok.Data;

import java.math.BigInteger;

@Data
public class MinerReward {
    private String id;
    private boolean proposal;
    private Integer pack_reward_tx;
    private Integer verifyBlock;
    private BigInteger pre_balance;
    private BigInteger currBalance;
    private BigInteger expectBalance;
    private String explain;

}
