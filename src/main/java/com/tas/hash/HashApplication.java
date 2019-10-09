package com.tas.hash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@SpringBootApplication
public class HashApplication {
    private ByteBuffer bb = ByteBuffer.allocate(204800);
    private void write(BigInteger value) {
        byte[] bs = new byte[0];
        if (value.intValue() != 0) {
            bs = value.toByteArray();
        }
        this.write(bs);
    }

    private void write(byte[] addr) {
        this.bb.putInt(addr.length);
        this.bb.put(addr);
    }

    private void write(byte type) {
        this.bb.put(type);
    }

    private void write(String data, String type) {
        byte[] bs;
        if (type.equals("addr")) {
            data = data.replace("zv", "");
           bs =  hexToByte(data);
           this.write(bs);
        } else if (type.equals("base64")) {
            Base64.Decoder decoder = Base64.getDecoder();
            bs = decoder.decode(data);
            this.write(bs);
        }
    }

    public static byte[] hexToByte(String hex){
        int m = 0, n = 0;
        int byteLen = hex.length() / 2;
        byte[] ret = new byte[byteLen];
        for (int i = 0; i < byteLen; i++) {
            m = i * 2 + 1;
            n = m + 1;
            int intVal = Integer.decode("0x" + hex.substring(i * 2, m) + hex.substring(m, n));
            ret[i] = (byte) intVal;
        }
        return ret;
    }

    public static byte[] sha256(byte[] input) {
        try {
            MessageDigest sha256digest = MessageDigest.getInstance("SHA-256");
            return sha256digest.digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if(hex.length() < 2){
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public void GenHash() {
        String src = "zvfd2f4785793a506225773ba7b036bfd55bb593c9dd16eaf52147a4ec963867af";
        String target = "zvfd2f4785793a506225773ba7b036bfd55bb593c9dd16eaf52147a4ec963867af";

        BigInteger value = new BigInteger("1000", 10);
        BigInteger gasLimit = new BigInteger("3000", 10);
        BigInteger gasPrice = new BigInteger("500", 10);
        BigInteger nonce = new BigInteger("0", 10);
        byte type = 0;
        String data = "AQ==";
        String extraData = "";

        this.write(src, "addr");
        this.write(target, "addr");
        this.write(value);
        this.write(gasLimit);
        this.write(gasPrice);
        this.write(nonce);
        this.write(type);
        this.write(data, "base64");
        this.write(extraData, "base64");
        byte[] bs = this.bb.array();
        byte[] res = new byte[this.bb.position()];
        System.arraycopy(bs, 0, res, 0, this.bb.position());
        byte[] hash = sha256(res);
        String s = bytesToHex(hash);
        System.out.println(s);
    }


    public static void main(String[] args) {



        HashApplication app = new HashApplication();
        app.GenHash();
        SpringApplication.run(HashApplication.class, args);
    }

}
