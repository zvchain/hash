# 使用说明



执行 

```shell
sh build/build.sh
```



目录下的hash.jar引入项目中


发送交易demo：

```java
    public static void main(String[] args) throws Exception {
        // 导入私钥地址
        ChainService.importKey("0xaeb220a575b99b43857954d214bf48746c6edc932aa0b1e03f34dbf75816a8d9");

        ChainService chainService = ChainService.getInstance("http://node1.zvchain.io:8101/", 5, 20);
        SignModel signModel = new SignModel();
        signModel.setValue(BigInteger.valueOf(100L));
        signModel.setSource("zv5fbf074e3482cd99666b94e3defdf0893aff0cd66608b0dc7d843dcb8da268ab");
        signModel.setTarget("zv5fbf074e3482cd99666b94e3defdf0893aff0cd66608b0dc7d843dcb8da268ab");
        String tranHash = chainService.sendTx(signModel);
        System.out.println(tranHash);
    }
```



其余接口详见:

com.zv.hash.zvchain.ChainBaseService



生成地址demo：

```java
public static void main(String[] args) {
        ZvcKeyPair zvcKeyPair = SecretKeyUtil.createAccount();
        System.out.println(zvcKeyPair);
    }
```

