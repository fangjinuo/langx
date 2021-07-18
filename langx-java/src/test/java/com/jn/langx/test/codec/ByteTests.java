package com.jn.langx.test.codec;

import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.util.Maths;
import com.jn.langx.util.Radixs;
import com.jn.langx.util.SystemPropertys;
import com.jn.langx.util.io.Charsets;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * 计算机是用二进制表示的，那么怎么用二进制来表示键盘上的字符呢？
 * 美国人发明了ASCII表，将键盘上的所有按键从0到127进行编号。
 * 这样每按下一个键，键盘就将每一个按键对应的数字编码的二进制信号发给主机。
 * 这样计算机在根据ASCII表就知道输入了什么，就可以显示到显示器上。
 *
 *  计算机内部是以二进制方式传输的，但二进制没法快速可视化，所以通常采用 8bit （字节）来表示，也就是 一个字节可以快速的表示一个 0-127 中的数字。
 *  也就是说 用byte可以更快的表达出一个很长的一段二进制数，这样可以加速信息理解与传输。
 *  另外计算机内部，比较早的时候，指令、数据总线的宽度都是8位，因此发送一次信息都要并行改变8个bit的状态。
 *
 * <p>
 * ASCII码只能表示0-127 这些数字，只能表达英文。但世界上有那么多种文字，于是有了Unicode 和UCS。
 * 他们编入各国的文字字符，这样每一个字符都对应了一个数字。
 * <p>
 * 也就是说，计算机表示一个字符，其实是根据一个码表，找到该字符的codepoint，也就是对应的数字。然后用数字来表示该字符的。
 * 那么可以理解为，任何一个字符，都有一个与之对应的数字（code point）。
 *
 * Unicode 和 UCS 共有两套来表示统一字符集的方案，Unicode使用的较多，所以大家很少听过UCS。
 *
 * ASCII、Unicode 、UCS 都只是一个码表，代表了字符与数字的映射表。码表本身不能定义如何进行存储、传输。
 * 但实际上在存储、传输过程中，用的是编码方式。
 * 我们常见的编码方式有：ASCII, ISO-8859-1, UTF-8, UTF-7,UTF-16, UTF-32 则是字符的编码方式。
 *
 * 他们按照各自的规则，将各自范围内的字符，用1到N个字符进行编码。认为识别时，在用响应的编码进行解码，解码后变成byte[]
 *
 * 编码过程：
 * 1）字符----> 通过Unicode|ASCII码表 -----> code point （是一个整数，计算机中同样是二进制表示）
 * 2）code point ----> 使用ASCII,UTF8,UTF-16等转换格式进行转换 -----> 转换成多个字节。
 */
public class ByteTests {
    @Test
    public void test() {
        printBytes("a".getBytes(Charsets.UTF_8));
        printBytes("a".getBytes(Charsets.US_ASCII));
    }

    private void printBytes(byte[] bytes) {
        printBytes(bytes, bytes.length);
    }

    private void printBytes(byte[] bytes, int length) {
        length = length < 0 ? 0 : length;
        length = Maths.min(length, bytes.length);
        for (int i = 0; i < length; i++) {
            System.out.print("," + bytes[i]);
        }
        System.out.println();
    }

    private void printBytesHex(byte[] bytes, int length) {
        length = length < 0 ? 0 : length;
        length = Maths.min(length, bytes.length);
        for (int i = 0; i < length; i++) {
            System.out.print("," + Radixs.toHex(bytes[i]));
        }
        System.out.println();
    }


    @Test
    public void test2() throws IOException {
        String workdir = SystemPropertys.getUserWorkDir();
        System.out.println(workdir);
        Resource resource = Resources.loadFileResource(workdir + "/src/test/java/com/jn/langx/test/codec/ByteTests.java");
        InputStream inputStream = resource.getInputStream();
        byte[] bytes = new byte[64];
        int length = -1;
        while ((length = inputStream.read(bytes)) != -1) {
            printBytesHex(bytes, length);
        }
    }
}
