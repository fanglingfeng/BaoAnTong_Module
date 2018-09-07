package com.tjsoft.util;

import com.google.common.hash.Hashing;

import java.util.Collections;
import java.util.List;

import cn.finalteam.toolsfinal.io.Charsets;

/**
 * Created by long on 2018/4/2.
 */

public class SignUtils {
    public static String sign(List<String> values, String ticket) {
        if (values == null) {
            throw new NullPointerException("values is null");
        }
        values.removeAll(Collections.singleton(null));// remove null
        values.add(ticket);
        java.util.Collections.sort(values);
        StringBuilder sb = new StringBuilder();
        for (String s : values) {
            sb.append(s);
        }
        return Hashing.sha1().hashString(sb,
                Charsets.UTF_8).toString().toUpperCase();
    }
}
