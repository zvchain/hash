package com.zv.hash.zvchain.model;

import lombok.Data;
import lombok.ToString;

import java.math.BigInteger;
import java.util.List;

@Data
@ToString
public class BlockDetail extends BlockHeader {
    private GenRewardTx genRewardTx;
    private List<Transaction> trans;
    private List<BodyRewardTxs> bodyRewardTxs;
    private List<MinerReward> minerReward;
    private BigInteger preTotalQn;
}
