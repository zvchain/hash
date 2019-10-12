package com.zv.hash.zvchain.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Receipt {
    private Integer status;
    private String cumulativeGasUsed;
    private String logs;
    private String transactionHash;
    private String contractAddress;
    private Integer height;
    private Integer txIndex;
}
