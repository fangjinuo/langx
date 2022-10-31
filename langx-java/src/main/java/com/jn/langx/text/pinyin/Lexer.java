package com.jn.langx.text.pinyin;

import com.jn.langx.io.buffer.CharSequenceBuffer;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.regexp.RegexpPatterns;
import com.jn.langx.util.regexp.Regexps;
import org.slf4j.Logger;

import java.util.List;

/**
 * 用于对一段文本分析。分析的结果是 一个token即可。
 * 分析过程中，会使用 标点符号（中、英文）对一段话进行分割，不会使用空格进行分割。
 *
 * @since 5.1.0
 */
class Lexer {
    private static final Logger logger = Loggers.getLogger(Lexer.class);
    private PinyinTokenizationConfig config;
    /**
     * 中文姓氏字典
     */
    private static PinyinDict chineseSurnameDict = PinyinDicts.getDict(PinyinDicts.DN_SURNAME);
    private static PinyinDict chinesePunctuationSymbolDict = PinyinDicts.getDict(PinyinDicts.DN_PUNCTUATION_SYMBOL);

    public void setConfig(PinyinTokenizationConfig config) {
        this.config = config;
    }

    /**
     * 对文本分析
     *
     * @param text
     * @return
     */
    public List<SegmentToken> analyze(String text) {
        if (Strings.isEmpty(text)) {
            return Collects.emptyArrayList();
        }
        List<SegmentToken> segments = Collects.emptyArrayList();

        CharSequenceBuffer csb = new CharSequenceBuffer(text);

        long segmentStart = -1;
        boolean isChineseSegment = false;
        while (csb.hasRemaining()) {
            String c = csb.get() + "";
            // 是中文 ？
            boolean isChinese = Regexps.match(RegexpPatterns.CHINESE_CHAR, c);
            // 是中文标点符号？
            boolean isChinesePunctuationSymbol = isChinese && Pinyins.isChinesePunctuationSymbol(c);
            boolean isEnglishPunctuationSymbol = Pinyins.isEnglishPunctuationSymbol(c);
            boolean isStopWord = isChinesePunctuationSymbol || isEnglishPunctuationSymbol;

            if (segmentStart < 0) {
                segmentStart = csb.position() - 1;
                isChineseSegment = isChinese;
            }
            boolean segmentFinished = isStopWord || !csb.hasRemaining() || isChineseSegment != isChinese;

            if (segmentFinished) {
                long segmentEnd = (csb.hasRemaining() || isStopWord) ? csb.position() - 1 : csb.position();

                if (!isChineseSegment) {
                    // 对非中文处理
                    if (segmentEnd <= segmentStart) {
                        continue;
                    }
                    long end = segmentEnd;
                    StringToken token = new StringToken();
                    String substring = csb.substring(segmentStart, end);
                    token.setBody(substring);
                    segments.add(token);

                } else {
                    // 对中文处理：
                    long start = segmentStart;
                    long end = segmentEnd;
                    PinyinDictItem surname = null;
                    if(start<end) {
                        ChineseSequenceToken chineseSequenceToken = new ChineseSequenceToken();
                        while (start < end) {
                            if (start + config.getTokenMaxChar() < end) {
                                // 避免总是进行无意义的查找
                                end = start + config.getTokenMaxChar();
                            }
                            String chineseWords = csb.substring(start, end);
                            PinyinDictItem item = find(surname, chineseWords);
                            if (item != null) {
                                if (config.isSurnameFirst() && surname == null) {
                                    surname = item;
                                }
                                chineseSequenceToken.addToken(item);
                                start = end;
                                end = segmentEnd;
                            } else {
                                end = end - 1;

                                if (end <= start) {
                                    // 汉字在字典里没找到，如果 dict/hanzi.dict 字典库足够全，可能这个代码片段就进不来了
                                    StringToken token = new StringToken();
                                    String w = csb.get(start) + "";
                                    logger.warn("Can't find the pinyin for {}", w);
                                    token.setBody(w);
                                    segments.add(token);
                                    start++;
                                    end = segmentEnd;
                                }

                            }
                        }
                        segments.add(chineseSequenceToken);
                    }
                }

                // 对停止词处理
                if (isStopWord) {
                    SegmentToken segmentToken = null;
                    if (isChinesePunctuationSymbol) {
                        ChineseSequenceToken chineseSequenceToken = new ChineseSequenceToken();
                        PinyinDictItem item = find(c, chinesePunctuationSymbolDict);
                        chineseSequenceToken.addToken(item);
                        segmentToken = chineseSequenceToken;
                    } else {
                        StringToken stringToken = new StringToken();
                        stringToken.setBody(c);
                        segmentToken = stringToken;
                    }
                    segments.add(segmentToken);
                    segmentToken.setPunctuationSymbol(true);
                }

                // 重置段开始
                segmentStart = -1;
                if (isChinese != isChineseSegment) {
                    segmentStart = isStopWord ? -1 : csb.position() - 1;
                    isChineseSegment = isChinese;
                }
            }
        }

        return segments;
    }

    private PinyinDictItem find(PinyinDictItem surname, String chineseWords, PinyinDict... dicts) {
        if (config.isSurnameFirst() && surname == null) {
            // 姓氏还没找到时，就是优先找姓氏
            surname = findSurname(chineseWords);
            if (surname == null) {
                surname = find(chineseWords, dicts);
            }
            return surname;
        }
        return find(chineseWords, dicts);
    }

    private PinyinDictItem findSurname(String chineseWords) {
        return find(chineseWords, chineseSurnameDict);
    }

    private PinyinDictItem find(String chineseWords, PinyinDict... dicts) {
        List<PinyinDict> _dicts = Objs.useValueIfEmpty(Collects.asList(dicts), config.getDicts());

        PinyinDictItem item = null;
        for (PinyinDict dict : _dicts) {
            item = dict.getItem(chineseWords);
            if (item != null) {
                break;
            }
        }
        return item;
    }
}
