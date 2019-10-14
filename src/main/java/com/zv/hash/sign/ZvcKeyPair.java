package com.zv.hash.sign;


import lombok.Data;

/**
 * @author lt
 * @Date 4:55 下午 2019/10/14
 */
@Data
public class ZvcKeyPair {
    private String secretKey;
    private String publicKey;
    private String zvcAddress;

    public ZvcKeyPair(String secretKey, String publicKey, String zvcAddress) {
        this.secretKey = "0x" + secretKey;
        this.publicKey = "0x04" + publicKey;
        this.zvcAddress = zvcAddress.toLowerCase();
    }
}
