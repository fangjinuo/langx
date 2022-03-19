package com.jn.langx.util.hash;

import com.jn.langx.security.crypto.digest.MessageDigests;

import java.security.MessageDigest;

public class MessageDigestHasher extends AbstractBytesResultHasher {
    public static final String HASHER_NAME_PREFIX="messagedigest-";
    private MessageDigest messageDigester;

    public MessageDigestHasher(String algorithm) {
        this.messageDigester = MessageDigests.newDigest(algorithm);
    }

    @Override
    public void update(byte[] bytes, int off, int len) {
        this.messageDigester.update(bytes, off, len);
    }

    @Override
    protected void reset() {
        super.reset();
        this.messageDigester.reset();
    }

    @Override
    public long getHash() {
        byte[] bytes = this.messageDigester.digest();
        reset();
        return toLong(bytes);
    }

    @Override
    protected Hasher createInstance(long seed) {
        throw new UnsupportedOperationException();
    }
}
