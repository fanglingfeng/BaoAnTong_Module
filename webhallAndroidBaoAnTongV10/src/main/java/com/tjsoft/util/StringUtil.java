package com.tjsoft.util;


public class StringUtil {
	
	/**
	 * <p>Checks if a CharSequence is whitespace, empty ("") or null.</p>
	 * 
	 * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     * 
	 * @param str the String to check, may be null
	 * @return
	 */
	public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }
	
	public static boolean isNotBlank(String str) {
        return !StringUtil.isBlank(str);
    }
	
	
	public static String toString(Object o) {
        return o!=null?o.toString():"";
    }
	
	public static String trim(Object o){
		return toString(o).trim();
	}
	
	
}
