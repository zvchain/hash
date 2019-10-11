package com.tas.hash.zvchain;

import com.tas.hash.sign.SignUtil;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lt
 * @Date 5:42 下午 2019/10/11
 */
public class AccountService {

    public static ConcurrentHashMap<String, String> keyMap = new ConcurrentHashMap<>(8);

    public static void importKey(String privateKey) {
        String zvcAddress = SignUtil.getAddress(privateKey);
        keyMap.putIfAbsent(zvcAddress.toLowerCase(), privateKey);
    }
}
