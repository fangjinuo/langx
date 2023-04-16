package com.jn.langx.util.net.ipv6;

import java.util.BitSet;

/**
 * This class contains some helpers for working with BitSets. These are generally not necessary in JDK7, since the BitSet.valueOf(long[])
 * method. However, for java-6 compatibility, we go this way.
 */
class BitSetHelpers {
    private BitSetHelpers() {
    }

    static BitSet bitSetOf(long lowerBits, long upperBits) {
        final BitSet bitSet = new BitSet();
        convert(lowerBits, 0, bitSet);
        convert(upperBits, Long.SIZE, bitSet);
        return bitSet;
    }

    static void convert(long value, int bitSetOffset, BitSet bits) {
        int index = 0;
        while (value != 0L) {
            if (value % 2L != 0) {
                bits.set(bitSetOffset + index);
            }
            ++index;
            value = value >>> 1;
        }
    }

}
