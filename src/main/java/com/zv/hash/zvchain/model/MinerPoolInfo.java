package com.zv.hash.zvchain.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class MinerPoolInfo {
    @SerializedName("tickets")
    private String tickets;

    @SerializedName("current_stake")
    private String current_stake;

    @SerializedName("full_stake")
    private String full_stake;

    @SerializedName("identity")
    private String identity;

    @SerializedName("valid_tickets")
    private String valid_tickets;
}
