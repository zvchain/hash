package com.zv.hash.sign;

import com.google.common.primitives.Bytes;
import com.zv.hash.util.MsgDigest;
import com.zv.hash.util.SHA3_256;
import net.consensys.cava.crypto.SECP256K1;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @author lt
 * @Date 5:25 下午 2019/10/10
 */
public class SignUtil {

    private static List<Byte> byteList = new ArrayList<>();

    public static String sign(String privateKey, com.zv.hash.sign.SignModel signModel) {
        if (privateKey.startsWith("0x")) {
            privateKey = privateKey.substring(2);
        }
        Security.addProvider(new BouncyCastleProvider());
        SECP256K1.KeyPair keyPair = SECP256K1.KeyPair.fromSecretKey(
                SECP256K1.SecretKey.fromInteger(new BigInteger(privateKey, 16)));

        String transactionHash = transactionHash(keyPair, signModel);
        return sign(keyPair, transactionHash);
    }

    private static String transactionHash(SECP256K1.KeyPair keyPair, SignModel signModel) {
        String source = getAddress(keyPair);
        String target = signModel.getTarget();
        BigInteger value = signModel.getValue();
        BigInteger gasLimit = signModel.getGasLimit();
        BigInteger gasPrice = signModel.getGasPrice();
        BigInteger nonce = signModel.getNonce();
        Byte type = signModel.getType();
        String data = signModel.getData();
        String extraData = signModel.getExtraData();

        write(source, true);
        write(target, true);
        write(value);
        write(gasLimit);
        write(gasPrice);
        write(nonce);
        write(type);
        write(data, false);
        write(extraData, false);

        byte[] bytes = Bytes.toArray(byteList);
        byte[] hash = sha256(bytes);
        return Hex.toHexString(hash);
    }

    private static void write(String source, boolean addressType) {
        byte[] bytes;
        if (addressType) {
            source = source.replace("zv", "");
            bytes = Hex.decode(source);
        } else {
            Base64.Decoder decoder = Base64.getDecoder();
            bytes = decoder.decode(source);
        }
        write(bytes);
    }


    private static void write(BigInteger param) {
        byte[] bytes = new byte[0];
        if (param.compareTo(BigInteger.ZERO) != 0) {
            bytes = param.toByteArray();
            bytes = removeSymbol(bytes);
        }
        write(bytes);
    }

    private static void write(byte[] bytes) {
        int length = bytes.length;
        for (byte b : intToByteArray(length)) {
            byteList.add(b);
        }
        for (byte aByte : bytes) {
            byteList.add(aByte);
        }
    }

    private static void write(byte type) {
        byteList.add(type);
    }

    private static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }

    private static String getAddress(SECP256K1.KeyPair keyPair) {
        MsgDigest digest = new SHA3_256();
        digest.update(keyPair.publicKey().bytesArray());
        return digest.toString();
    }

    public static String getAddress(String privateKey) {
        Security.addProvider(new BouncyCastleProvider());
        if (privateKey.startsWith("0x")) {
            privateKey = privateKey.substring(2);
        }
        SECP256K1.KeyPair keyPair = SECP256K1.KeyPair.fromSecretKey(
                SECP256K1.SecretKey.fromInteger(new BigInteger(privateKey, 16)));
        MsgDigest digest = new SHA3_256();
        digest.update(keyPair.publicKey().bytesArray());
        return "zv" + digest.toString();
    }


    private static byte[] sha256(byte[] input) {
        try {
            MessageDigest sha256digest = MessageDigest.getInstance("SHA-256");
            return sha256digest.digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    private static String sign(SECP256K1.KeyPair keyPair, String hash) {
        byte[] msgHash = Hex.decode(hash);
        List<Byte> bytes = new ArrayList<>();
        SECP256K1.Signature signature = SECP256K1.signHashed(msgHash, keyPair);
        for (byte b : signature.r().toByteArray()) {
            bytes.add(b);
        }
        for (byte b : signature.s().toByteArray()) {
            bytes.add(b);
        }
        bytes.add(signature.v());
        byte[] res = Bytes.toArray(bytes);
        res = removeSymbol(res);
        return "0x" + Hex.toHexString(res);
    }

    private static byte[] removeSymbol(byte[] bytes) {
        if (bytes[0] == 0) {
            byte[] res = new byte[bytes.length - 1];
            System.arraycopy(bytes, 1, res, 0, bytes.length - 1);
            bytes = res;
        }
        return bytes;
    }
}
