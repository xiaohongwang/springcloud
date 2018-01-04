package com.shiro;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.BlowfishCipherService;
import org.apache.shiro.crypto.DefaultBlockCipherService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.format.HashFormat;
import org.apache.shiro.crypto.hash.format.HashFormatFactory;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;

/**
 * Shiro提供了base64和16进制字符串编码/解码的API支持
 */
public class ShiroEncryTest{
    private static final Logger log =
            LoggerFactory.getLogger(ShiroEncryTest.class);

    @Test
    /**
     *base64和16进制字符串编码/解码
     */
    public void testBase64(){
        String str = "shiro";
        String base64Str = Base64.encodeToString(str.getBytes());
        String str2 = Base64.decodeToString(base64Str);
        Assert.assertEquals(str, str2);
    }

    @Test
    /**
     * 16进制字符串编码/解码操作
     */
    public void testHex(){
        String str = "shiro";
        String hexStr = Hex.encodeToString(str.getBytes());
        String str2 = new String(Hex.decode(hexStr.getBytes()));
        Assert.assertEquals(str, str2);
    }

    @Test
    /**
     * CodecSupport，
     * 提供了toBytes(str, "utf-8") / toString(bytes, "utf-8")
     * 用于在byte数组/String之间转换。
     */
    public void testCodecSupport(){
        String str = "您好Shiro";
        byte[] bytes =  CodecSupport.toBytes(str,"utf-8");

        String str2  =CodecSupport.toString(bytes, "utf-8");
        Assert.assertEquals(str, str2);
    }

    @Test
    public void testRandom() {
        //生成随机数
        SecureRandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
        randomNumberGenerator.setSeed("123".getBytes());
        System.out.println(randomNumberGenerator.nextBytes().toHex());
    }

    @Test
    /**
     * SHA256算法生成相应的散列数据 Sha1 Sha384 Sha512
     */
    public void testSha256(){
        String str = "hello";
        String salt = "123";
        String sha1 = new Sha256Hash(str, salt).toString();
        log.info(sha1);
    }

    @Test
    /**
     * Shiro提供了HashService，默认提供了DefaultHashService实现
     * 实质使用SimpleHash
     */
    public void testHashService(){
        DefaultHashService hashService = new DefaultHashService(); //默认算法SHA-512
        hashService.setHashAlgorithmName("SHA-512");
        hashService.setPrivateSalt(new SimpleByteSource("123")); //私盐，默认无
        hashService.setGeneratePublicSalt(true);//是否生成公盐，默认false
        hashService.setRandomNumberGenerator(new SecureRandomNumberGenerator());//用于生成公盐。默认就这个
        hashService.setHashIterations(1); //生成Hash值的迭代次数

        HashRequest request = new HashRequest.Builder()
                .setAlgorithmName("MD5").setSource(ByteSource.Util.bytes("hello"))
                .setSalt(ByteSource.Util.bytes("123")).setIterations(2).build();
        String hex = hashService.computeHash(request).toHex();


    }

    @Test
    public void testAES(){
        AesCipherService aesCipherService = new AesCipherService();
        aesCipherService.setKeySize(128); //设置key长度
        //生成key
        Key key = aesCipherService.generateNewKey();
        //source
        String text = "hello";
        //加密
        String encrptText =
                aesCipherService.encrypt(text.getBytes(), key.getEncoded()).toHex();
        //解密
        String text2 =
                new String(aesCipherService.decrypt(Hex.decode(encrptText), key.getEncoded()).getBytes());

        Assert.assertEquals(text, text2);
    }

    @Test
    public void testBlowfishCipherService() {
        BlowfishCipherService blowfishCipherService = new BlowfishCipherService();
        blowfishCipherService.setKeySize(128);

        //生成key
        Key key = blowfishCipherService.generateNewKey();

        String text = "hello";

        //加密
        String encrptText = blowfishCipherService.encrypt(text.getBytes(), key.getEncoded()).toHex();
        //解密
        String text2 = new String(blowfishCipherService.decrypt(Hex.decode(encrptText), key.getEncoded()).getBytes());

        Assert.assertEquals(text, text2);
    }

    @Test
    public void testDefaultBlockCipherService() throws Exception {


        //对称加密，使用Java的JCA（javax.crypto.Cipher）加密API，常见的如 'AES', 'Blowfish'
        DefaultBlockCipherService cipherService = new DefaultBlockCipherService("AES");
        cipherService.setKeySize(128);

        //生成key
        Key key = cipherService.generateNewKey();

        String text = "hello";

        //加密
        String encrptText = cipherService.encrypt(text.getBytes(), key.getEncoded()).toHex();
        //解密
        String text2 = new String(cipherService.decrypt(Hex.decode(encrptText), key.getEncoded()).getBytes());

        Assert.assertEquals(text, text2);
    }


    @Test
    public void testPasswordService(){
        DefaultPasswordService passwordService =
                    new DefaultPasswordService();
       String encrypassword =
               passwordService.encryptPassword("123");

    }
}
