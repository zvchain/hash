package com.tas.hash.zvchain.model;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class MinerInfo {
    private List<StakeInfo> overview;

    @Data
    @ToString
    public class StakeInfo {
        private Integer stake;
        private String type;
        private String miner_status;
        private Long status_update_height;
    }
}
