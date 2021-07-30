package com.jn.langx.security.gm.tests.gmssl;

import com.jn.langx.codec.hex.Hex;
import com.jn.langx.util.io.Charsets;
import org.gmssl.GmSSL;
import org.junit.Test;

public class SM3Tests {
    private String string = "abcde_12345";
    private GmSSL gmSSL = new GmSSL();

    @Test
    public void test() {
        byte[] s3 = gmSSL.digest("SM3", string.getBytes(Charsets.UTF_8));
        System.out.println(Hex.encodeHexString(s3));
    }
}
