package com.zv.hash.sign;

import net.consensys.cava.crypto.SECP256K1;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.security.Security;

/**
 * @author lt
 * @Date 4:54 下午 2019/10/14
 */
public class SecretKeyUtil {

    public static ZvcKeyPair createAccount() {
        Security.addProvider(new BouncyCastleProvider());
        SECP256K1.KeyPair keyPair = SECP256K1.KeyPair.random();
        String secretKey = Hex.toHexString(keyPair.secretKey().bytesArray());
        String publicKey = Hex.toHexString(keyPair.publicKey().bytesArray());
        String zvcAddress = SignUtil.getAddress(secretKey);
        return new ZvcKeyPair(secretKey, publicKey, zvcAddress);
    }


}
