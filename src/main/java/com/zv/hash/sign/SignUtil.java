package com.zv.hash.sign;

import com.google.common.primitives.Bytes;
import net.consensys.cava.crypto.SECP256K1;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * @author lt
 * @Date 5:25 下午 2019/10/10
 */
public class SignUtil {

    private static String ZVC_FLAG = "zv";
    private static String HEX_FLAG = "0x";
    private static Integer HEX_NUMBER = 16;

    private static List<Byte> byteList = new ArrayList<>();

    public static String sign(String privateKey, com.zv.hash.sign.SignModel signModel) {
        if (privateKey.startsWith(HEX_FLAG)) {
            privateKey = privateKey.substring(2);
        }
        Security.addProvider(new BouncyCastleProvider());
        SECP256K1.KeyPair keyPair = SECP256K1.KeyPair.fromSecretKey(
                SECP256K1.SecretKey.fromInteger(new BigInteger(privateKey, HEX_NUMBER)));

        String transactionHash = transactionHash(keyPair, signModel);
        byteList.clear();
        return sign(keyPair, transactionHash);
    }

    private static String transactionHash(SECP256K1.KeyPair keyPair, SignModel signModel) {
        String source = getAddress(keyPair);
        String target = signModel.getTarget();
        BigInteger value = signModel.getValue().toBigInteger();
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
            source = source.replace(ZVC_FLAG, "");
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
        SHA3.Digest256 digest = new SHA3.Digest256();
        digest.update(filter(keyPair.publicKey().bytesArray()));
        return Hex.toHexString(digest.digest());
    }

    public static String getAddress(String privateKey) {
        Security.addProvider(new BouncyCastleProvider());
        if (privateKey.startsWith(HEX_FLAG)) {
            privateKey = privateKey.substring(2);
        }
        SECP256K1.KeyPair keyPair = SECP256K1.KeyPair.fromSecretKey(
                SECP256K1.SecretKey.fromInteger(new BigInteger(privateKey, HEX_NUMBER)));
        return ZVC_FLAG + getAddress(keyPair);
    }

    private static byte[] filter(byte[] bytes) {
        int remainder = bytes.length % 2;
        if (remainder == 0) {
            int halfSize = bytes.length / 2;
            byte[] byteFront = Arrays.copyOfRange(bytes, 0, halfSize);
            byte[] byteBehind = Arrays.copyOfRange(bytes, halfSize, bytes.length);
            byteFront = removeSymbol(byteFront);
            byteBehind = removeSymbol(byteBehind);
            ByteBuffer byteBuffer = ByteBuffer.allocate(byteFront.length + byteBehind.length);
            byteBuffer.put(byteFront);
            byteBuffer.put(byteBehind);
            return byteBuffer.array();
        } else {
            return bytes;
        }
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
        for (byte b : checkout(signature.r().toByteArray())) {
            bytes.add(b);
        }
        for (byte b : checkout(signature.s().toByteArray())) {
            bytes.add(b);
        }
        bytes.add(signature.v());
        byte[] res = Bytes.toArray(bytes);
        return HEX_FLAG + Hex.toHexString(res);
    }

    private static byte[] checkout(byte[] bytes) {
        int length = bytes.length;
        if (length == 32) {
            return bytes;
        } else if (length < 32) {
            byte[] result = new byte[32];
            System.arraycopy(bytes, 0, result, 32 - bytes.length, bytes.length);
            return result;
        } else {
            byte[] result = new byte[32];
            System.arraycopy(bytes, bytes.length - 32, result, 0, 32);
            return result;
        }
    }

    private static byte[] removeSymbol(byte[] bytes) {
        while (bytes[0] == 0) {
            byte[] res = new byte[bytes.length - 1];
            System.arraycopy(bytes, 1, res, 0, bytes.length - 1);
            bytes = res;
        }
        return bytes;
    }
}
