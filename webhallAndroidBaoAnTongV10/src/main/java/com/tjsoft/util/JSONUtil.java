package com.tjsoft.util;
import com.google.gson.Gson;

public class JSONUtil {
	private static Gson gson;

	public static Gson getGson() {
		if (null == gson) {
			gson = new Gson();
			return gson;
		} else {
			return gson;
		}
	}
	

	

}
