package com.jn.langx.test.security.key;

import com.jn.langx.security.ext.cryptojs.CryptoJS;
import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.Lists;
import org.junit.Test;

import java.util.List;

public class CryptoJsMockTests {
    @Test
    public void mockAES(){
        String message="test@123";
        String passphrase="NsFoCus$#";
        String encryptedText=CryptoJS.AES.encrypt(message,passphrase,null);
        System.out.println(encryptedText);


        String decryptedText = CryptoJS.AES.decrypt(encryptedText, passphrase,null);
        System.out.println(decryptedText);
        System.out.println(Objs.equals(message,decryptedText));

        System.out.println("==========接下来的测试是用 node.js 使用 crypto-js 生成的加密数据 ==========");
        List<String> encryptedTexts= Lists.newArrayList(
                "U2FsdGVkX18vE/xKFEjExOkwa5LGi23wj5ZXXR5zNJs=",
                "U2FsdGVkX1+EkiwP7zttFvziuAOM0MDoPYvKcIxvz3w="
        );
        for (String cipherText: encryptedTexts ){
            decryptedText = CryptoJS.AES.decrypt(encryptedText, passphrase,null);
            System.out.println(decryptedText);
            System.out.println(Objs.equals(message,decryptedText));
        }
    }
}