package com.jn.langx.util.hash.streaming;

import com.jn.langx.util.hash.AbstractHasher;

/**
 * @since 4.4.0
 */
public class Fnv1_64Hasher extends AbstractStreamingHasher {
    private final static long INITIAL_VALUE = 0xcbf29ce484222325L;
    private final static long MULTIPLIER = 0x100000001b3L;

    private long hash = INITIAL_VALUE;


    @Override
    public void update(byte b) {
        hash *= MULTIPLIER;
        hash ^= 0xffL & b;
    }


    @Override
    public long getHash() {
        long h = hash;
        reset();
        return h;
    }

    @Override
    public void reset() {
        hash = INITIAL_VALUE;
    }

    @Override
    protected AbstractHasher createInstance(Object initParam) {
        return new Fnv1_64Hasher();
    }
}
