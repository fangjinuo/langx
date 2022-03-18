package com.jn.langx.util.hash;

import static com.google.common.base.Preconditions.checkPositionIndexes;

public class Murmur3_32Hasher extends Hasher {
    private static final int CHUNK_SIZE = 4;

    private int h;
    private long buffer;
    private int shift;
    private int length;

    // Finalization mix - force all bits of a hash block to avalanche
    private static int fmix(int h1, int length) {
        h1 ^= length;
        h1 ^= h1 >>> 16;
        h1 *= 0x85ebca6b;
        h1 ^= h1 >>> 13;
        h1 *= 0xc2b2ae35;
        h1 ^= h1 >>> 16;
        return h1;
    }

    private static final int UNSIGNED_MASK = 0xFF;

    public static int toInt(byte value) {
        return value & UNSIGNED_MASK;
    }

    private static final int C1 = 0xcc9e2d51;
    private static final int C2 = 0x1b873593;

    private static int getIntLittleEndian(byte[] input, int offset) {
        return fromBytes(input[offset + 3], input[offset + 2], input[offset + 1], input[offset]);
    }

    /**
     * Returns the {@code int} value whose byte representation is the given 4 bytes, in big-endian
     * order; equivalent to {@code Ints.fromByteArray(new byte[] {b1, b2, b3, b4})}.
     */
    private static int fromBytes(byte b1, byte b2, byte b3, byte b4) {
        return b1 << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8 | (b4 & 0xFF);
    }

    private static int mixK1(int k1) {
        k1 *= C1;
        k1 = Integer.rotateLeft(k1, 15);
        k1 *= C2;
        return k1;
    }

    private static int mixH1(int h1, int k1) {
        h1 ^= k1;
        h1 = Integer.rotateLeft(h1, 13);
        h1 = h1 * 5 + 0xe6546b64;
        return h1;
    }

    @Override
    public void update(byte[] bytes, int off, int len) {
        checkPositionIndexes(off, off + len, bytes.length);
        int i;
        for (i = 0; i + 4 <= len; i += 4) {
            update(4, getIntLittleEndian(bytes, off + i));
        }
        for (; i < len; i++) {
            update(bytes[off + i]);
        }
    }

    @Override
    protected void update(byte b) {
        update(1, b & 0xFF);
    }

    private void update(int nBytes, long update) {
        // 1 <= nBytes <= 4
        buffer |= (update & 0xFFFFFFFFL) << shift;
        shift += nBytes * 8;
        length += nBytes;

        if (shift >= 32) {
            this.h = mixH1(this.h, mixK1((int) buffer));
            buffer >>>= 32;
            shift -= 32;
        }
    }

    @Override
    public void setSeed(long seed) {
        super.setSeed(seed);
        this.h = (int) seed;
    }

    @Override
    protected void reset() {
        super.reset();
        this.h = -1;
        this.buffer = 0;
        this.shift = 0;
    }

    @Override
    public long get() {
        this.h ^= mixK1((int) buffer);
        this.h = fmix(this.h, length);
        return this.h;
    }
}
