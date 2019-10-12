package com.zv.hash.zvchain.model;

import lombok.Data;

import java.math.BigInteger;

@Data
public class BlockHeightReward {
    private String proposal_id;

    private BigInteger proposal_reward;

    private BigInteger proposal_gas_fee_reward;

    private BigInteger verifier_gas_fee_reward;
}
