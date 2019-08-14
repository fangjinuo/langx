package com.jn.langx.util.struct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entry<K, V> extends Pair<K, V> implements Map.Entry<K, V> {
    public Entry(K key) {
        setKey(key);
    }

    public Entry(K key, V value) {
        setKey(key);
        setValue(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Entry)) {
            return false;
        }
        @SuppressWarnings("rawtypes")
        Entry o = (Entry) obj;
        return getKey().equals(o.getKey()) && getValue().equals(o.getValue());
    }

    @Override
    public int hashCode() {
        return getKey().hashCode() ^ 4 + getValue().hashCode();
    }

    public static Entry<String, String> newEntry(String keyValue, String spec)
            throws IllegalArgumentException {
        if (spec == null || spec.isEmpty()) {
            throw new IllegalArgumentException("argument 'spec' is null .");
        }
        if (keyValue == null || keyValue.isEmpty()) {
            throw new IllegalArgumentException("argument 'keyValue' is null .");
        }
        int index = keyValue.indexOf(spec);
        if (index == -1) {
            return new Entry<String, String>(keyValue.trim(), "");
        }
        return new Entry<String, String>(keyValue.substring(0, index).trim(),
                keyValue.substring(index + spec.length()).trim());
    }

    public static Map<String, String> getMap(String str, String keyValueSpec,
                                             String entrySpec) {
        Map<String, String> map = new HashMap<String, String>();
        if (str == null || str.isEmpty()) {
            return map;
        }
        String[] entryArray = str.split(entrySpec);
        Entry<String, String> entry = null;
        for (String keyValue : entryArray) {
            try {
                entry = Entry.newEntry(keyValue, keyValueSpec);
            } catch (IllegalArgumentException ex) {
                entry = null;
            }
            if (entry != null) {
                map.put(entry.getKey(), entry.getValue());
            }
        }

        return map;
    }

    public static List<Map<String, String>> getMapList(String src,
                                                       String keyValueSpec, String entrySpec, String listSpecFlag) {
        List<String> strList = new ArrayList<String>();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        if (listSpecFlag == null || listSpecFlag.isEmpty()) {
            strList.add(src);
        } else {
            int index = src.indexOf(listSpecFlag);
            if (index == -1) {
                return list;
            }

            int nextIndex = -1;
            while ((nextIndex = src.indexOf(listSpecFlag,
                    index + listSpecFlag.length())) != -1) {
                strList.add(src.substring(index, nextIndex));
                index = nextIndex;
            }
            strList.add(src.substring(index));
        }

        for (String str : strList) {
            Map<String, String> map = getMap(str, keyValueSpec, entrySpec);
            list.add(map);
        }

        return list;
    }
}