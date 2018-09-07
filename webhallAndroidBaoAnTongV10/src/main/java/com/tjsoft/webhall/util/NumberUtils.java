package com.tjsoft.webhall.util;

/**
 * Created by lenovo on 2016/12/14.
 */

public class NumberUtils {
    public static boolean isNumeric(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }
}
