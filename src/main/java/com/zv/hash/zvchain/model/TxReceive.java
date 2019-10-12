package com.zv.hash.zvchain.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TxReceive {
    @SerializedName("Receipt")
    private Receipt receipt;
    @SerializedName("Transaction")
    private Transaction transaction;
}
