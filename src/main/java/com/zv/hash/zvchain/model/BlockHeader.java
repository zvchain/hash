package com.zv.hash.zvchain.model;

import lombok.Data;
import lombok.ToString;

/**
 * @author kaede
 * @create 2019-08-15 17:51
 **/
@Data
@ToString
public class BlockHeader {
    private Integer height;
    private String hash;
    private String pre_hash;
    private String cur_time;
    private String pre_time;
    private String castor;
    private String groupId;
    private String prove;
    private Integer total_qn;
    private Integer qn;
    private Integer txs;
    private String stateRoot;
    private String tx_root;
    private String receiptRoot;
    private String prove_root;
    private String random;
}
