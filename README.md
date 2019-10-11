# 使用说明



执行 

```shell
sh build/build.sh
```



目录下的hash.jar引入项目中

项目目录下新建zvc.properties文件，配置zvc节点地址：

```
zvc.chain.url=http://node1.zvchain.io:8101/
```



发送交易demo：

```java
public static void main(String[] args) throws Exception {
        ChainUtilsService chainUtilsService = ChainUtilsService.getInstance();
        SignModel signModel = new SignModel();
        signModel.setValue(BigInteger.valueOf(100L));
        signModel.setPrivateKey("0xaeb220a575b99b43857954d214bf48746c6edc932aa0b1e03f34dbf75816a8d9");
        signModel.setTarget("zv5fbf074e3482cd99666b94e3defdf0893aff0cd66608b0dc7d843dcb8da268ab");
        String tranHash = chainUtilsService.sendTx(signModel);
        System.out.println(tranHash);
    }
```

