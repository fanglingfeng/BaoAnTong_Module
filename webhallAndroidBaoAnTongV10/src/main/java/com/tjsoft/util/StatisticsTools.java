package com.tjsoft.util;

import com.tjsoft.webhall.constants.Constants;

public class StatisticsTools {

	public static void start() {
		if (null != Constants.getInstance().getGloabDelegete()) {
			Constants.getInstance().getGloabDelegete().startStatistics();
		}
	}

	public static void end(String ParentName, String CategoryName, String ModelName) {
		if (null != Constants.getInstance().getGloabDelegete()) {
			Constants.getInstance().getGloabDelegete().endStatistics(ParentName, CategoryName, ModelName);
		}
	}

}
