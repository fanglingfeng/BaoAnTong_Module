package com.tjsoft.util;

import android.app.DatePickerDialog;
import android.content.Context;

/**
 * 日期选择器，解决点击返回键不调用onDateSet()方法
 * 
 * @author huangmingqiao
 * @version 1.0
 * @since 1.0
 * @date 2015-03-16
 * 
 */
public class MyDatePickerDialog extends DatePickerDialog {
	public MyDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
		super(context, callBack, year, monthOfYear, dayOfMonth);
	}

	@Override
	protected void onStop() {
		// super.onStop();
	}
}
